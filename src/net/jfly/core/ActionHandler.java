package net.jfly.core;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.jfly.handler.Handler;

/**
 * ActionHandler
 */
final class ActionHandler extends Handler {
	public static Logger logger = Logger.getLogger(ActionHandler.class.getName());

	private final static boolean devMode = Config.getConstants().isDevMode();
	private final ActionMapping actionMapping;

	public ActionHandler(ActionMapping actionMapping) {
		this.actionMapping = actionMapping;
	}

	/**
	 * handle 1: Action action = actionMapping.getAction(target) 2: new
	 * ActionInvocation(...).invoke() 3: render(...)
	 */
	@Override
	public void handle(String requestUrl, boolean[] isHandled, HttpServletRequest request, HttpServletResponse response) {
		if (requestUrl.indexOf(".") != -1) {
			return;
		}

		isHandled[0] = true;
		String[] urlParamArray = { null };
		Action action = actionMapping.getAction(requestUrl, urlParamArray);

		if (action == null) {

			String queryString = request.getQueryString();
			if (queryString == null) {
				logger.log(Level.FINE, "Action 不能找到,请求信息Url：" + (queryString == null ? requestUrl : requestUrl + "?" + queryString));
			}
			// 404页面跳转
			return;
		}

		Controller controller = null;
		try {
			controller = action.getControllerClass().newInstance();
		} catch (InstantiationException e) {
			throw new RuntimeException("不能实例化:" + action.getClass(), e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException("不能实例化:" + action.getClass(), e);
		}
		controller.init(request, response, urlParamArray[0]);
		{// 构建ActionInvocation
			if (devMode) {
				boolean isMultipartRequest = ActionReporter.commonRequestReport(controller, action);
				if (isMultipartRequest)
					ActionReporter.multipartRequestReport(controller, action);
			}
			new ActionInvocation(action, controller).invoke();
		}
	}
}
