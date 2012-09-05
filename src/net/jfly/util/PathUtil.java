package net.jfly.util;

import java.io.File;

public class PathUtil {

	// 针对类级别信息
	@SuppressWarnings("rawtypes")
	public static String getClassSimpleName(Class clazz) {
		return clazz.getSimpleName();
	}

	@SuppressWarnings("rawtypes")
	public static String getClassPackageName(Class clazz) {
		return clazz.getPackage().getName();
	}

	@SuppressWarnings("rawtypes")
	public static String getClassWholeName(Class clazz) {
		return clazz.getName();
	}

	// 类物理路径信息
	/**
	 * <pre>
	 *  eg:/D:/lxy/JFly/WebRoot/WEB-INF/classes/
	 * `""`表示`classes/`
	 * </pre>
	 */
	@SuppressWarnings("rawtypes")
	public static String getClassFolderUrlPath(Class clazz) {

		return clazz.getClassLoader().getResource("").getPath();
	}

	/**
	 * <pre>
	 * eg:D:\lxy\JFly\WebRoot\WEB-INF\classes
	 * </pre>
	 */
	@SuppressWarnings("rawtypes")
	public static String getClassFolderWindowsPath(Class clazz) {
		String urlPath = clazz.getClassLoader().getResource("").getPath();
		return new File(urlPath).getPath();
	}

	// WebRoot设置
	private static String webRootPath;

	public static String getWebRootPath() {
		return webRootPath;
	}

	public static void setWebRootPath(String webRootPath) {
		// 必须包含分割符
		if (webRootPath.endsWith(File.separator)) {
			PathUtil.webRootPath = webRootPath;
		} else {
			PathUtil.webRootPath = webRootPath + File.separator;
		}
	}

	public static void main(String[] args) {
		System.out.println(getClassFolderWindowsPath(PathUtil.class));

	}
}
