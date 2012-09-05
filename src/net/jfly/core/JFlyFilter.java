package net.jfly.core;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * JFly框架过滤器
 */
public final class JFlyFilter implements Filter {

	private String charset = Config.getConstants().getCharacterEncoding();

	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@SuppressWarnings("unused")
	private void createJFlyConfig(String configClass) {
		if (configClass == null)
			throw new RuntimeException("请在web.xml配置JFly所需的configClass");

		try {
			Object configClassInstance = Class.forName(configClass).newInstance();
			if (configClassInstance == null) {
				throw new RuntimeException("请在web.xml配置JFly所需的configClass:" + configClass + "不能正确的实例化");
			}
		} catch (Exception e) {
			throw new RuntimeException("请在web.xml正确配置JFly所需的configClass", e);
		}
	}

	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		// 编码设置
		request.setCharacterEncoding(charset);
		response.setCharacterEncoding(charset);

		try {
			// 进行处理
		} catch (Exception e) {
			String queryString = request.getQueryString();
		}
	}

	public void destroy() {
		// 如果系统中存在依赖其他很消耗资源的组件，请在这里关闭

	}
}
