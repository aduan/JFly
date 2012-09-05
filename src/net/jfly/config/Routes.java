package net.jfly.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.jfly.core.Controller;

/**
 * 路由配置
 */
public abstract class Routes {

	private final Map<String, Class<? extends Controller>> controllerMap = new HashMap<String, Class<? extends Controller>>();
	private final Map<String, String> viewPathMap = new HashMap<String, String>();
	private static String baseViewPath;

	public abstract void config();

	final public Routes add(Routes routes) {
		if (routes != null) {
			routes.config();
			controllerMap.putAll(routes.controllerMap);
			viewPathMap.putAll(routes.viewPathMap);
		}
		return this;
	}

	/**
	 * 增加路由信息
	 */
	final public Routes add(String controllerKey, Class<? extends Controller> controllerClass, String viewPath) {
		if (controllerKey == null) {
			throw new IllegalArgumentException("controllerKey " + controllerKey + "不能为空");
		}
		controllerKey = controllerKey.trim();
		if ("".equals(controllerKey)) {
			throw new IllegalArgumentException("controllerKey " + controllerKey + "不能为空");
		}

		if (controllerClass == null) {
			throw new IllegalArgumentException("controllerClass " + controllerClass + "不能为空");
		}
		if (controllerMap.containsKey(controllerKey)) {
			throw new IllegalArgumentException("controllerClass" + controllerClass + "已经存在");
		}

		if (!controllerKey.startsWith("/")) {
			controllerKey = "/" + controllerKey;
		}
		controllerMap.put(controllerKey, controllerClass);

		// viewPath默认是controllerKey
		if (viewPath == null || "".equals(viewPath.trim())) {
			viewPath = controllerKey;
		}

		viewPath = viewPath.trim();
		// 一定保证前缀为"/"
		if (!viewPath.startsWith("/")) {
			viewPath = "/" + viewPath;
		}
		// 一定保证后缀为"/"
		if (!viewPath.endsWith("/")) {
			viewPath = viewPath + "/";
		}

		if (baseViewPath != null) {
			viewPath = baseViewPath + viewPath;
		}
		viewPathMap.put(controllerKey, viewPath);
		return this;
	}

	final public Routes add(String controllerkey, Class<? extends Controller> controllerClass) {
		return add(controllerkey, controllerClass, controllerkey);
	}

	final public Set<Entry<String, Class<? extends Controller>>> getEntrySet() {
		return controllerMap.entrySet();
	}

	final public String getViewPath(String key) {
		return viewPathMap.get(key);
	}

	/**
	 * 制定项目的基础路径baseViewPath
	 */
	static void setBaseViewPath(String baseViewPath) {
		if (baseViewPath == null) {
			throw new IllegalArgumentException("baseViewPath不能为空");
		}
		baseViewPath = baseViewPath.trim();
		if ("".equals(baseViewPath)) {
			throw new IllegalArgumentException("baseViewPath不能为空");
		}
		if (!baseViewPath.startsWith("/")) {
			baseViewPath = "/" + baseViewPath;
		}
		/**
		 * <pre>
		 * API:substring：Returns a new string that is a substring of this string. The substring begins at the specified beginIndex and extends to the character at index endIndex - 1. Thus the length of the substring is endIndex-beginIndex.
		 * </pre>
		 */
		if (baseViewPath.endsWith("/")) {
			baseViewPath = baseViewPath.substring(0, baseViewPath.length() - 1);
		}
		Routes.baseViewPath = baseViewPath;
	}
}
