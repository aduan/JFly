package net.jfly.plugin.DataSource;

import java.beans.PropertyVetoException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.sql.DataSource;

import net.jfly.plugin.IPlugin;
import net.jfly.plugin.ActiveRecord.IDataSourceProvider;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class C3p0Plugin implements IPlugin, IDataSourceProvider {
	private String driverClass = "com.mysql.jdbc.Driver";
	private String jdbcUrl;
	private String username;
	private String password;

	private int initialPoolSize = 10;
	private int minPoolSize = 10;
	private int maxPoolSize = 32;

	private int acquireIncrement = 5;
	private int maxIdleTime = 6000;

	private ComboPooledDataSource dataSource;

	/**
	 * ~1.C3p0属性直接设置设置
	 */
	public C3p0Plugin(String jdbcUrl, String username, String password) {
		this.jdbcUrl = jdbcUrl;
		this.username = username;
		this.password = password;
	}

	public C3p0Plugin(String jdbcUrl, String username, String password, String driverClass) {
		this.jdbcUrl = jdbcUrl;
		this.username = username;
		this.password = password;
		this.driverClass = driverClass != null ? driverClass : this.driverClass;
	}

	public C3p0Plugin(String jdbcUrl, String username, String password, String driverClass, Integer maxPoolSize, Integer minPoolSize, Integer initialPoolSize, Integer maxIdleTime, Integer acquireIncrement) {
		initC3p0Properties(jdbcUrl, username, password, driverClass, maxPoolSize, minPoolSize, initialPoolSize, maxIdleTime, acquireIncrement);
	}

	/**
	 * ~2.从属性文件里面读取C3p0配置信息。建议使用这种
	 */
	public C3p0Plugin(File propertyFile) {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(propertyFile);
			Properties p = new Properties();
			p.load(fis);
			initC3p0Properties(p.getProperty("driverClass"), p.getProperty("jdbcUrl"), p.getProperty("username"), p.getProperty("password"), toInt(p.getProperty("initialPoolSize")), toInt(p.getProperty("minPoolSize")), toInt(p.getProperty("maxPoolSize")), toInt(p.getProperty("acquireIncrement")), toInt(p.getProperty("maxIdleTime")));
		} catch (Exception e) {
			throw new RuntimeException("C3p0数据源配置出错：" + e.getMessage());
		} finally {
			if (fis != null)
				try {
					fis.close();
				} catch (IOException e) {
					throw new RuntimeException("C3p0数据源配置文件不能关闭：" + e.getMessage());
				}
		}
	}

	/**
	 * ~3.直接传入Properties
	 */
	public C3p0Plugin(Properties properties) {
		Properties p = properties;
		initC3p0Properties(p.getProperty("jdbcUrl"), p.getProperty("user"), p.getProperty("password"), p.getProperty("driverClass"), toInt(p.getProperty("maxPoolSize")), toInt(p.getProperty("minPoolSize")), toInt(p.getProperty("initialPoolSize")), toInt(p.getProperty("maxIdleTime")), toInt(p.getProperty("acquireIncrement")));
	}

	/**
	 * 核心方法。最终都是调用这个方法
	 */
	private void initC3p0Properties(String driverClass, String jdbcUrl, String username, String password, Integer initialPoolSize, Integer minPoolSize, Integer maxPoolSize, Integer acquireIncrement, Integer maxIdleTime) {
		this.driverClass = driverClass != null ? driverClass : this.driverClass;
		this.jdbcUrl = jdbcUrl;
		this.username = username;
		this.password = password;

		this.initialPoolSize = initialPoolSize != null ? initialPoolSize : this.initialPoolSize;
		this.minPoolSize = minPoolSize != null ? minPoolSize : this.minPoolSize;
		this.maxPoolSize = maxPoolSize != null ? maxPoolSize : this.maxPoolSize;

		this.acquireIncrement = acquireIncrement != null ? acquireIncrement : this.acquireIncrement;
		this.maxIdleTime = maxIdleTime != null ? maxIdleTime : this.maxIdleTime;

	}

	public boolean start() {
		dataSource = new ComboPooledDataSource();
		try {
			dataSource.setDriverClass(driverClass);
			dataSource.setJdbcUrl(jdbcUrl);
			dataSource.setUser(username);
			dataSource.setPassword(password);

			dataSource.setInitialPoolSize(initialPoolSize);
			dataSource.setMinPoolSize(minPoolSize);
			dataSource.setMaxPoolSize(maxPoolSize);

			dataSource.setAcquireIncrement(acquireIncrement);
			dataSource.setMaxIdleTime(maxIdleTime);

		} catch (PropertyVetoException e) {
			dataSource = null;
			throw new RuntimeException("C3p0数据源获取失败" + e.getMessage());
		}
		return true;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public boolean stop() {
		if (dataSource != null) {
			dataSource.close();
			return true;
		} else {
			return false;
		}

	}

	private Integer toInt(String string) {
		return Integer.parseInt(string);
	}

}
