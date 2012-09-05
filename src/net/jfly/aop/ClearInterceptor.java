package net.jfly.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ClearInterceptor 能够清除施加在该对象上面不同层次的拦截器:Upper或者All.默认Upper
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
public @interface ClearInterceptor {
	ClearLayer value() default ClearLayer.Upper;
}
