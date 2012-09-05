package net.jfly.core;

import java.lang.reflect.Method;

import net.jfly.aop.Interceptor;

/**
 * ActionInvocation用于保证Action的执行
 */
public class ActionInvocation {
	// 保证JVM运行正常
	private static final Object[] Null_Array = new Object[0];
	private Controller controller;
	private Interceptor[] interceptors;

	private Action action;
	private int index = 0;

	// ActionInvocationWrapper[即ActionInvocation封装器需要这个]
	protected ActionInvocation() {

	}

	ActionInvocation(Action action, Controller controller) {
		this.controller = controller;
		this.interceptors = action.getInterceptors();
		this.action = action;
	}

	public void invoke() {
		if (index < interceptors.length)
			// bug
			interceptors[index++].intercept();
		else if (index++ == interceptors.length)
			try {
				action.getMethod().invoke(controller, Null_Array);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
	}

	/**
	 * 说明：actionKey = controllerKey + methodName
	 */
	public String getActionKey() {
		return action.getActionKey();
	}

	public String getControllerKey() {
		return action.getControllerKey();
	}

	public Controller getController() {
		return controller;
	}

	public String getMethodName() {
		return action.getMethodName();
	}

	public Method getMethod() {
		return action.getMethod();
	}

	public String getViewPath() {
		return action.getViewPath();
	}
}
