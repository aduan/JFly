package net.jfly.config;

/**
 * 开发者常量设置
 */
public class Constants {
	// 本类分为几个部分.
	// 1:框架开发调试
	private boolean devMode = false;

	/**
	 *设置开发模式启用与否,便于在开发过程中对项目运行信息的处理
	 */
	public void setDevMode(boolean devMode) {
		this.devMode = devMode;
	}

	public boolean isDevMode() {
		return devMode;
	}

}
