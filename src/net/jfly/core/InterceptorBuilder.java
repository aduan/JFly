package net.jfly.core;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import net.jfly.aop.Before;
import net.jfly.aop.ClearInterceptor;
import net.jfly.aop.ClearLayer;
import net.jfly.aop.Interceptor;

/**
 * 拦截器构建者
 */
class InterceptorBuilder {

	private static final Interceptor[] Null_Interceptor_Array = new Interceptor[0];
	private Map<Class<Interceptor>, Interceptor> interceptorMap = new HashMap<Class<Interceptor>, Interceptor>();

	@SuppressWarnings("unchecked")
	void addToInterceptorsMap(Interceptor[] defaultInterceptors) {
		for (Interceptor interceptor : defaultInterceptors) {
			interceptorMap.put((Class<Interceptor>) interceptor.getClass(), interceptor);
		}
	}

	/**
	 * 创建拦截器:每个实例化的拦截器只产生一个实例。
	 */
	private Interceptor[] createInterceptors(Before beforeAnnotation) {
		Interceptor[] result = null;
		@SuppressWarnings("unchecked")
		// :Before(values={xx.class,yy.class})
		Class<Interceptor>[] interceptorClasses = (Class<Interceptor>[]) beforeAnnotation.value();
		if (interceptorClasses != null && interceptorClasses.length > 0) {
			result = new Interceptor[interceptorClasses.length];
			for (int i = 0; i < result.length; i++) {
				result[i] = interceptorMap.get(interceptorClasses[i]);
				if (result[i] != null) {
					continue;
				}
				try {
					result[i] = interceptorClasses[i].newInstance();
					interceptorMap.put(interceptorClasses[i], result[i]);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}
		return result;
	}

	/**
	 * 构建控制器的拦截器
	 */
	Interceptor[] buildControllerInterceptors(Class<? extends Controller> controllerClass) {
		Before before = controllerClass.getAnnotation(Before.class);
		return before != null ? createInterceptors(before) : Null_Interceptor_Array;
	}

	/**
	 * 构建方法的拦截器
	 */
	Interceptor[] buildMethodInterceptors(Method method) {
		Before before = method.getAnnotation(Before.class);
		return before != null ? createInterceptors(before) : Null_Interceptor_Array;
	}

	/**
	 * 得到放在方法上面标记的ClearInterceptor标记里面的值
	 */
	private ClearLayer getMethodClearType(Method method) {
		ClearInterceptor clearInterceptor = method.getAnnotation(ClearInterceptor.class);
		return clearInterceptor != null ? clearInterceptor.value() : null;
	}

	/**
	 * 得到放在控制器上面标记的ClearInterceptor标记里面的值
	 */
	private ClearLayer getControllerClearType(Class<? extends Controller> controllerClass) {
		ClearInterceptor clearInterceptor = controllerClass.getAnnotation(ClearInterceptor.class);
		return clearInterceptor != null ? clearInterceptor.value() : null;
	}

	/**
	 * 构建Action的拦截器
	 */
	Interceptor[] buildActionInterceptors(Interceptor[] defaultInterceptors, Class<? extends Controller> controllerClass, Interceptor[] controllerInterceptors, Method method, Interceptor[] methodInterceptors) {
		ClearLayer controllerClearType = getControllerClearType(controllerClass);
		if (controllerClearType != null) {
			defaultInterceptors = Null_Interceptor_Array;
		}

		ClearLayer methodClearType = getMethodClearType(method);
		if (methodClearType != null) {
			controllerInterceptors = Null_Interceptor_Array;
			if (methodClearType == ClearLayer.All) {
				defaultInterceptors = Null_Interceptor_Array;
			}
		}
		int size = defaultInterceptors.length + controllerInterceptors.length + methodInterceptors.length;
		if (size == 0) {
			return Null_Interceptor_Array;
		} else {
			Interceptor[] result = new Interceptor[size];
			int index = 0;
			for (int i = 0; i < defaultInterceptors.length; i++) {
				result[index++] = defaultInterceptors[i];
			}
			for (int i = 0; i < controllerInterceptors.length; i++) {
				result[index++] = controllerInterceptors[i];
			}
			for (int i = 0; i < methodInterceptors.length; i++) {
				result[index++] = methodInterceptors[i];
			}
			return result;
		}

	}

}
