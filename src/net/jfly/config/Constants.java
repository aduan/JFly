package net.jfly.config;

/**
 * 开发者常量设置
 */
public class Constants {
	// 本类分为几个部分.
	// 1:框架开发调试
	private boolean devMode = false;
	private String characterEncoding = "utf-8";
	private boolean canScannerWebCodeAndData = true;
	private boolean isShowErrorInfo = true;
	private boolean isThrowErrorInfo = true;

	// 2:框架配置常量
	private String urlParamSeparator = "-";

	/**
	 * 设置开发模式启用与否,便于在开发过程中对项目运行信息的处理
	 */
	public void setDevMode(boolean devMode) {
		this.devMode = devMode;
	}

	public boolean isDevMode() {
		return devMode;
	}

	public void setUrlParamSeparator(String urlParamSeparator) {
		this.urlParamSeparator = urlParamSeparator;
	}

	public String getUrlParamSeparator() {
		return urlParamSeparator;
	}

	public String getCharacterEncoding() {
		return characterEncoding;
	}

	public void setCharacterEncoding(String characterEncoding) {
		this.characterEncoding = characterEncoding;
	}

	public boolean isCanScannerWebCodeAndData() {
		return canScannerWebCodeAndData;
	}

	public void setCanScannerWebCodeAndData(boolean canScannerWebCodeAndData) {
		this.canScannerWebCodeAndData = canScannerWebCodeAndData;
	}

	public boolean isShowErrorInfo() {
		return isShowErrorInfo;
	}

	public void setShowErrorInfo(boolean isShowErrorInfo) {
		this.isShowErrorInfo = isShowErrorInfo;
	}

	public boolean isThrowErrorInfo() {
		return isThrowErrorInfo;
	}

	public void setThrowErrorInfo(boolean isThrowErrorInfo) {
		this.isThrowErrorInfo = isThrowErrorInfo;
	}

}
