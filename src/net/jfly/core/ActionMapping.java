package net.jfly.core;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.jfly.aop.Interceptor;
import net.jfly.config.Interceptors;
import net.jfly.config.Routes;

final class ActionMapping {

	private static final String Slash = "/";
	private Routes routes;
	private Interceptors interceptors;

	private final Map<String, Action> actionMapping = new HashMap<String, Action>();

	ActionMapping(Routes routes, Interceptors interceptors) {
		this.routes = routes;
		this.interceptors = interceptors;
	}

	// --------------------------------构建ActionMapping--------------------------------
	// 取得`Controller.class`参数个数为0方法集合
	private Set<String> buildExcludedMethodName() {
		Set<String> excludedMethodName = new HashSet<String>();
		Method[] methods = Controller.class.getMethods();
		for (Method method : methods) {
			if (method.getParameterTypes().length == 0) {
				excludedMethodName.add(method.getName());
			}
		}
		return excludedMethodName;
	}

	// 通过Routes和构建Interceptors构建Action
	void buildActionMapping() {
		Set<String> excludedMethodName = buildExcludedMethodName();
		InterceptorBuilder interceptorBuilder = new InterceptorBuilder();
		Interceptor[] defaultInterceptors = interceptors.getInterceptorArray();
		// 将defaultInterceptors转变为InterceptorMap
		interceptorBuilder.addToInterceptorsMap(defaultInterceptors);

		for (Entry<String, Class<? extends Controller>> entry : routes.getEntrySet()) {
			// 控制器:
			Class<? extends Controller> controllerClass = entry.getValue();
			Interceptor[] controllerInterceptors = interceptorBuilder.buildControllerInterceptors(controllerClass);
			Method[] methods = controllerClass.getMethods();
			// 方法:
			for (Method method : methods) {
				String methodName = method.getName();
				/**
				 * <pre>
				 * !excludedMethodName.contains(methodName) && method.getParameterTypes().length == 0表示：Controller自定义的方法
				 * </pre>
				 */
				if (!excludedMethodName.contains(methodName) && method.getParameterTypes().length == 0) {

					Interceptor[] methodInterceptors = interceptorBuilder.buildMethodInterceptors(method);
					/**
					 * <pre>
					 * 对照: interceptorBuilder.buildActionInterceptors(defaultInters, controllerInters, controllerClass, methodInters, method);
					 * </pre>
					 */
					// Action:
					Interceptor[] actionInterceptors = interceptorBuilder.buildActionInterceptors(defaultInterceptors, controllerClass, controllerInterceptors, method, methodInterceptors);

					String controllerKey = entry.getKey();
					// 框架风格：默认方法为index即：X/
					if (methodName.equals("index")) {
						String actionKey = controllerKey;
						/**
						 * <pre>
						 * 说明:注意顺序:public Action(String actionKey, String controllerKey, Class<? extends Controller> controllerClass, String methodName, Method method, Interceptor[] interceptors, String viewPath) {}
						 * </pre>
						 */
						Action action = new Action(actionKey, controllerKey, controllerClass, methodName, method, actionInterceptors, routes.getViewPath(controllerKey));
						action = actionMapping.put(actionKey, action);
						// 表示已经存在这个Action实例
						if (action != null) {
							warnning(action.getActionKey(), action.getControllerClass(), action.getMethod());
							throw new RuntimeException("Action:" + action.getActionKey() + "已经存在. Controller/index重复.");
						}
					} else {
						/**
						 * <pre>
						 *  如果controllerKey为`/`,则actionKey为/method,否则为/Controller/Method
						 * </pre>
						 */
						String actionKey = controllerKey.equals(Slash) ? Slash + methodName : controllerKey + Slash + methodName;

						if (actionMapping.containsKey(actionKey)) {
							warnning(actionKey, controllerClass, method);
							continue;
						}

						Action action = new Action(actionKey, controllerKey, controllerClass, methodName, method, actionInterceptors, routes.getViewPath(controllerKey));
						actionMapping.put(actionKey, action);
					}
				}
			}
		}
		// 系统关键点:关于`""`的处理.解决办法是将`/`的Action赋值给`""`
		Action actoin = actionMapping.get("/");
		if (actoin != null) {
			actionMapping.put("", actoin);
		}
	}

	/**
	 * 警告但是不终止程序
	 */
	private static final void warnning(String actionKey, Class<? extends Controller> controllerClass, Method method) {
		StringBuilder sb = new StringBuilder();
		sb.append("\n警告:ActionKey:" + actionKey + "已经被使用").append("Action 不能匹配方法:").append(controllerClass.getName()).append(".").append(method.getName()).append("()");
		System.out.println(sb.toString());
	}

	// --------------------------------取得具体某个Action实例--------------------------------
	List<String> getAllActionKeys() {
		List<String> allActionKeys = new ArrayList<String>(actionMapping.keySet());
		Collections.sort(allActionKeys);
		return allActionKeys;
	}

	/**
	 * <pre>
	 * 支持 四种类型的URL
	 * 1: http://abc.com/controllerKey                  ---> 00
	 * 2: http://abc.com/controllerKey/param            ---> 01
	 * 3: http://abc.com/controllerKey/method           ---> 10
	 * 4: http://abc.com/controllerKey/method/param     ---> 11
	 * 说明第2和3想极为麻烦,需要根据URL分隔符进行区分.
	 * </pre>
	 */
	Action getAction(String url, String[] urlParam) {
		Action action = actionMapping.get(url);
		if (action != null) {
			return action;
		}

		int i = url.lastIndexOf(Slash);
		if (i != -1) {
			action = actionMapping.get(url.substring(0, i));
			urlParam[0] = url.substring(i + 1);
		}

		return action;
	}

}
