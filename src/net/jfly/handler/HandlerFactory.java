package net.jfly.handler;

import java.util.List;

public class HandlerFactory {
	public static Handler getHandler(List<Handler> handlerList, Handler actionHandler) {
		Handler result = actionHandler;

		for (int i = handlerList.size() - 1; i >= 0; i--) {
			Handler handler = handlerList.get(i);
			handler.nextHandler = result;
			result = handler;
		}

		return result;
	}
}
