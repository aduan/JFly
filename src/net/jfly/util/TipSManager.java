package net.jfly.util;

import net.jfly.config.Constants;
import net.jfly.core.Config;

public class TipSManager {
	private static Constants constants = Config.getConstants();
	private static boolean isShowErrorInfo = constants.isShowErrorInfo();
	private static boolean isThrowErrorInfo = constants.isThrowErrorInfo();

	/**
	 * 通过配置来打印异常
	 */
	public static void showErrorInfo(Exception e) {
		if (isShowErrorInfo) {
			e.printStackTrace();
		}
	}

	/**
	 * 通过配置来抛出异常
	 */
	public static void throwErrorInfo(Exception e) {
		if (isThrowErrorInfo) {
			throw new RuntimeException(e);
		}

	}

	/**
	 * 通过配置来打印和抛出异常
	 */
	public static void showAndThrowErrorInfo(Exception e) {
		if (isShowErrorInfo) {
			e.printStackTrace();
		}
		if (isThrowErrorInfo) {
			throw new RuntimeException(e);
		}

	}

}
