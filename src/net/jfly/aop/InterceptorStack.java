package net.jfly.aop;

import java.util.ArrayList;
import java.util.List;

/**
 * 拦截器栈.使用的设计模式是责任链
 */
public abstract class InterceptorStack implements Interceptor {

	@SuppressWarnings("unused")
	private Interceptor[] interceptors;
	private List<Interceptor> interceptorList;

	protected final InterceptorStack addInterceptors(Interceptor... interceptors) {
		if (interceptors == null || interceptors.length == 0) {
			throw new IllegalArgumentException("添加的拦截器不能为空");
		}

		if (interceptorList == null) {
			interceptorList = new ArrayList<Interceptor>();
		}

		for (Interceptor interceptor : interceptors) {
			interceptorList.add(interceptor);
		}

		return this;
	}

	public abstract void config();

	public InterceptorStack() {
		config();
		if (interceptorList == null) {
			throw new RuntimeException("请调用 addInterceptors(...)方法来配置InterceptorStack()方法");
		}

		interceptors = interceptorList.toArray(new Interceptor[interceptorList.size()]);
		interceptorList.clear();
		interceptorList = null;
	}

}
