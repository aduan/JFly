package net.jfly.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 通过配置Handler[处理程序],可以帮助Action做许多方便的事
 */
public abstract class Handler {

	protected Handler nextHandler;

	/**
	 * <pre>
	 * 对request,response进行处理。如果{`isHandled[0] == false`}表示JFlyFilter将会调用doFilter()。通常是让Filter 处理 静态资源.
	 * </pre>
	 */
	public abstract void handle(String requestUrl, boolean[] isHandled, HttpServletRequest request, HttpServletResponse response);
}
