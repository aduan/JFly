package net.jfly.plugin.ActiveRecord;

import javax.sql.DataSource;

/**
 * ActiveRecordPlugin 构造方法 会需要 DataSourceProvider和DataSource数据源
 */
public interface IDataSourceProvider {
	DataSource getDataSource();
}
