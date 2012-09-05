package net.jfly.core;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import net.jfly.aop.Interceptor;

/**
 * Action信息显示器
 */
final class ActionReporter {

	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/**
	 * 普通http请求Action信息显示
	 */
	static final boolean commonRequestReport(Controller controller, Action action) {
		String ContentType = controller.getRequest().getContentType();
		// ContentType.toLowerCase().indexOf("multipart") == -1表示不是简单的http请求
		if (ContentType == null || ContentType.toLowerCase().indexOf("multipart") == -1) {
			doReport(controller, action);
			return false;
		}
		return true;
	}

	/**
	 * 非普通http请求Action信息显示
	 */
	static final void multipartRequestReport(Controller controller, Action action) {
		doReport(controller, action);
	}

	private static final void doReport(Controller controller, Action action) {
		StringBuilder sb = new StringBuilder("\n--------------------------------JFly Action 信息报告--------------------------------");
		sb.append("\n\n----------信息当前时间:		").append(sdf.format(new Date()));
		Class<? extends Controller> controllerClass = action.getControllerClass();
		sb.append("\n\n----------当前控制器:		").append(controllerClass.getName()).append("	即(").append(controllerClass.getSimpleName()).append(".java文件)");
		sb.append("\n\n----------调用方法:		").append(action.getMethodName());

		String urlParam = controller.getUrlParam();
		if (urlParam != null) {
			sb.append("\n\n----------Url参数 :		").append(urlParam);
		} else {
			sb.append("\n\n----------Url参数 :为空		");
		}
		// 拦截器信息
		sb.append("\n\n----------该Action拦截器信息显示开始		");
		Interceptor[] interceptors = action.getInterceptors();
		if (interceptors.length > 0) {

			for (int i = 0; i < interceptors.length; i++) {
				sb.append("\n----------拦截器[" + i + "]:		");
				Interceptor interceptor = interceptors[i];
				Class<? extends Interceptor> interceptorClass = interceptor.getClass();
				sb.append(interceptorClass.getName()).append("	即(").append(interceptorClass.getSimpleName()).append(".java)");
			}
		} else {
			sb.append("\n----------该Action拦截器信息无,如果你在该Action中已经添加拦截器信息，建议你检查拦截器配置是否正确");
		}
		sb.append("\n----------该Action拦截器信息显示结束		");
		// 参数信息
		sb.append("\n\n----------该Action参数信息显示开始		");
		HttpServletRequest request = controller.getRequest();
		@SuppressWarnings("unchecked")
		Enumeration<String> e = request.getParameterNames();
		if (e.hasMoreElements()) {
			String name = e.nextElement();
			String[] values = request.getParameterValues(name);
			if (values.length == 1) {
				sb.append("\n----------只有一个参数:" + name).append("=").append(values[0]);
			} else {
				{// 一行显示
					sb.append("\n----------参数信息[一行]显示:" + name).append("[]={");
					for (int i = 0; i < values.length; i++) {
						if (i > 0) {
							sb.append("    ,	");
						}
						sb.append(values[i]);
					}
					sb.append("}");
				}
				{// 多行显示
					sb.append("\n----------参数信息[多行]显示如下:" + name).append("[]");
					for (int i = 0; i < values.length; i++) {
						sb.append("\n----------" + name + "[" + i + "]		").append(values[i]);
					}
				}
			}
		}
		sb.append("\n----------该Action参数信息显示结束		");
		System.out.print(sb.toString());
	}
}
