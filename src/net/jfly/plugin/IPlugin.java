package net.jfly.plugin;

/**
 * JFLy采用插件机制,开发者可以将功能强大的组件添加到框架中
 */
public interface IPlugin {
	boolean start();

	boolean stop();
}