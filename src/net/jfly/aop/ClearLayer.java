package net.jfly.aop;

/**
 * <pre>
 * ClearLayer即[添加了拦截器]对象清除施加在其身的拦截器层次:upper和all . JFly 拦截器针对的对象分为3种:Global,Controller and Action.
 * </pre>
 */
public enum ClearLayer {

	/**
	 * 清除施加在该对象上面的上一级别的拦截器
	 */
	Upper,

	/**
	 * 清除施加在该对象上面的所有拦截器
	 */
	All;
}
