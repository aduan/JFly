package net.jfly.config;

import java.util.ArrayList;
import java.util.List;

import net.jfly.aop.Interceptor;

/**
 * Interceptors将Interceptor应用应用到所有Action
 */
final public class Interceptors {

	private final List<Interceptor> interceptorList = new ArrayList<Interceptor>();

	public Interceptors add(Interceptor interceptor) {
		if (interceptor != null) {
			this.interceptorList.add(interceptor);
		}
		return this;
	}

	public Interceptor[] getInterceptorArray() {
		Interceptor[] result = interceptorList.toArray(new Interceptor[interceptorList.size()]);// 定义一个恰当大小的数组new
																								// Interceptor[interceptorList.size()]
		return result == null ? new Interceptor[0] : result;
	}
}
