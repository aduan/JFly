package net.jfly.server;

import net.jfly.util.PathUtil;

public class ServerFactory {
	// 必须设置一个常量控制是否启动自动项目重新部署。

	private static final int Default_Port = 80;
	private static final int Default_ScanIntervalSeconds = 4;

	public ServerFactory() {
	}

	// 用户配置
	public static IServer getServer(String webRootDir, int port, String contextPath, int scanIntervalSeconds) {
		return new JettyServer(webRootDir, port, contextPath, scanIntervalSeconds);
	}

	// 完全默认
	public static IServer getServer() {
		return getServer(getWebRootFolderPath(), Default_Port, "/", Default_ScanIntervalSeconds);
	}

	/**
	 * 即项目在Myeclipse开发时"AppName/"物理路径文件夹名
	 */
	private static String getWebRootFolderPath() {
		// 当前框架开发测试时结果:D:\lxy\JFly\WebRoot\WEB-INF\classes-->WebRoot
		String rootClassPath = PathUtil.getClassFolderWindowsPath(PathUtil.class);
		String[] temp = null;
		if (rootClassPath.indexOf("\\WEB-INF\\") != -1) {
			temp = rootClassPath.split("\\\\");
		} else {
			// Linux支持
			temp = rootClassPath.split("/");
		}
		return temp[(temp.length - 1) - 2];

	}
}
