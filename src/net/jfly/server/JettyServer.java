package net.jfly.server;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.management.MBeanServer;

import net.jfly.core.Config;
import net.jfly.util.PathUtil;

import org.mortbay.jetty.Server;
import org.mortbay.jetty.nio.SelectChannelConnector;
import org.mortbay.jetty.webapp.WebAppContext;
import org.mortbay.management.MBeanContainer;
import org.mortbay.util.Scanner;

/**
 * JettyServer在JFly框架里面用于项目开发时候的自动重新部署 [Jetty version 6.1.26]
 */
class JettyServer implements IServer {

	public static long changTimes = 1;
	/**
	 * Myeclipse中项目的根目录webRootDir是`WebRoot/`
	 */
	private String webRootDir;
	private int port = 80;
	private String contextPath;
	private int scanIntervalSeconds = 4;

	// 默认不允许扫描
	private boolean canScannerWebCodeAndData = Config.getConstants().isCanScannerWebCodeAndData();

	private Server server;
	private WebAppContext webAppContext;
	private boolean isStarted = false;

	JettyServer(String webAppDir, int port, String contextPath, int scanIntervalSeconds) {
		this.webRootDir = webAppDir;
		this.port = port;
		this.contextPath = contextPath;
		this.scanIntervalSeconds = scanIntervalSeconds;
	}

	private void checkConfig() {
		{// 可以默认设置
			{// ~检查端口是否被占用
				boolean portIsAvailable = false;
				try {
					portIsAvailable = portIsAvailable(port);
				} catch (IOException e) {
					// 不能将IOException改成其他异常
					throw new RuntimeException("端口检测出现异常" + e.getMessage());
				}
				if (!portIsAvailable) {
					throw new RuntimeException("端口被占用");
				}
			}
			// 间隔太小：容易引起错误
			if (scanIntervalSeconds < 4) {
				canScannerWebCodeAndData = false;
				System.out.println("自动监听项目改变重新部署的功能关闭");
			}
		}
		{// 必须开发者设置
			if (contextPath == null) {
				throw new IllegalStateException("服务器ContextPath不能为空");
			}
			if (webRootDir == null) {
				throw new IllegalStateException("服务器webRootDir不能为空");
			}
		}

	}

	private static boolean portIsAvailable(int port) throws IOException {
		if (port <= 0 || port > 65536) {
			throw new IllegalArgumentException("不合法端口: " + port + ",建议使用80或者8080端口");
		}
		ServerSocket serverSocket = null;
		DatagramSocket datagramSocket = null;
		try {
			serverSocket = new ServerSocket(port);
			serverSocket.setReuseAddress(true);

			datagramSocket = new DatagramSocket(port);
			datagramSocket.setReuseAddress(true);
			return true;
		} catch (IOException e) {
			return false;
		} finally {
			if (datagramSocket != null) {
				datagramSocket.close();
			}
			if (serverSocket != null) {
				serverSocket.close();
			}
		}

	}

	// 调用这个方法，用于启动该JettyServer组件.启动前必须检查配置是否正确
	public void start() {
		try {
			if (canScannerWebCodeAndData) {
				// 这个方法是运用出现异常则下面不能执行的原理
				checkConfig();
				if (!isStarted) {
					doStart();
					isStarted = true;
				} else {
					System.out.println("项目已经部署.");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void doStart() throws Exception {
		// contextPath局部作用域
		String contextPath = this.contextPath;
		String webAppDir = this.webRootDir;
		Integer port = this.port;
		Integer scanIntervalSeconds = this.scanIntervalSeconds;

		server = new Server();
		if (port != null) {
			SelectChannelConnector connector = new SelectChannelConnector();
			connector.setPort(port);
			server.addConnector(connector);
		}

		webAppContext = new WebAppContext();
		webAppContext.setContextPath(contextPath);
		webAppContext.setWar(webAppDir);
		webAppContext.setInitParams(Collections.singletonMap("org.mortbay.jetty.servlet.Default.useFileMappedBuffer", "false"));
		server.addHandler(webAppContext);
		/**
		 * <pre>
		 * JMX是Java Management Extension的缩写，其实简单得说来就是用来监控Java Class运行时状态的技术.见本包里面的详细说明
		 * </pre>
		 */
		MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
		MBeanContainer mBeanContainer = new MBeanContainer(mBeanServer);
		mBeanContainer.start();

		server.getContainer().addEventListener(mBeanContainer);

		final ArrayList<File> scanList = new ArrayList<File>();
		// 原理是：只要Class文件夹发生改变，马上重新部署
		scanList.add(new File(PathUtil.getClassFolderWindowsPath(PathUtil.class)));

		// 关键
		Scanner scanner = new Scanner();
		scanner.setReportExistingFilesOnStartup(false);
		scanner.setScanInterval(scanIntervalSeconds);
		scanner.setScanDirs(scanList);
		try {
			scanner.addListener(new Scanner.BulkListener() {

				public void filesChanged(@SuppressWarnings("rawtypes") List changes) {
					try {
						System.out.println("项目代码改变,正在重新部署......");
						try {
							webAppContext.stop();
						} catch (Throwable e) {
						}
						System.out.println("			这是你启动框架开发项目第" + changTimes + "次添加或者修改代码");
						changTimes++;
						try {
							webAppContext.start();
						} catch (Throwable e) {
							// do nothing
						}
						System.out.println("项目代码改变,重新部署成功......");

					} catch (Throwable e) {
						System.out.println("新部署失败,请检查你的代码是否正确，或者检查你的配置是否正确");
					}
				}
			});
		} catch (Throwable e) {

		}
		scanner.start();
		System.err.println("扫描器已经启动,每隔" + scanIntervalSeconds + "秒进行扫描一次");
		try {
			try {
				server.start();
				// JettyServer日志输出java.lang.ExceptionInInitializerError[bug]解决办法.
				for (int i = 0; i < 10000; i++) {
					System.out.println();
				}
			} catch (Throwable e) {
				System.out.println("bug");
			}
			server.join();
		} catch (Throwable e) {
			// 必须停止JVM,保证出现异常后,项目不会继续向数据库等其他数据资源里面读写数据
			System.exit(100);
		}
		return;
	}

}
