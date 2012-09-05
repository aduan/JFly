package net.jfly.core;

import java.lang.reflect.Method;

import net.jfly.aop.Interceptor;

/**
 * 使用Action处理用户请求
 */
class Action {
	private final String actionKey;

	private final Class<? extends Controller> controllerClass;
	private final String controllerClassKey;

	private final String methodName;
	private final Method method;

	private final Interceptor[] interceptors;
	private final String viewPath;

	public Action(String actionKey, String controllerKey, Class<? extends Controller> controllerClass, String methodName, Method method, Interceptor[] interceptors, String viewPath) {
		this.actionKey = actionKey;

		this.controllerClassKey = controllerKey;
		this.controllerClass = controllerClass;

		this.methodName = methodName;
		this.method = method;

		this.interceptors = interceptors;
		this.viewPath = viewPath;
	}

	public String getActionKey() {
		return actionKey;
	}

	public String getControllerKey() {
		return controllerClassKey;
	}

	public Class<? extends Controller> getControllerClass() {
		return controllerClass;
	}

	public String getMethodName() {
		return methodName;
	}

	public Method getMethod() {
		return method;
	}

	public Interceptor[] getInterceptors() {
		return interceptors;
	}

	public String getViewPath() {
		return viewPath;
	}

}
