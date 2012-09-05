package net.jfly.plugin.ActiveRecord;

import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

import net.jfly.plugin.IPlugin;
import net.jfly.plugin.ActiveRecord.dialect.Dialect;

public class ActiveRecordPlugin implements IPlugin {

	private static DataSource dataSource;
	private static IDataSourceProvider dataSourceProvider;
	private static final List<TableInfo> tableMappings = new ArrayList<TableInfo>();

	public ActiveRecordPlugin setDialect(Dialect dialect) {
		if (dialect != null)
			DbKit.setDialect(dialect);
		return this;
	}

	public ActiveRecordPlugin(IDataSourceProvider dataSourceProvider) {
		ActiveRecordPlugin.dataSourceProvider = dataSourceProvider;
	}

	public ActiveRecordPlugin(IDataSourceProvider dataSourceProvider, int transactionLevel) {
		ActiveRecordPlugin.dataSourceProvider = dataSourceProvider;
		DbKit.setTransactionLevel(transactionLevel);
	}

	public ActiveRecordPlugin(DataSource dataSource) {
		ActiveRecordPlugin.dataSource = dataSource;
	}

	public ActiveRecordPlugin(DataSource dataSource, int transactionLevel) {
		ActiveRecordPlugin.dataSource = dataSource;
		DbKit.setTransactionLevel(transactionLevel);
	}

	public ActiveRecordPlugin addMapping(String tableName, String primaryKey, Class<? extends Model<?>> modelClass) {
		tableMappings.add(new TableInfo(tableName, primaryKey, modelClass));
		return this;
	}

	public ActiveRecordPlugin addMapping(String tableName, Class<? extends Model<?>> modelClass) {
		tableMappings.add(new TableInfo(tableName, modelClass));
		return this;
	}

	/**
	 * Please use addMapping method making code shorter.
	 */
	@Deprecated
	public ActiveRecordPlugin addTableMapping(String tableName, String primaryKey, Class<? extends Model<?>> modelClass) {
		return addMapping(tableName, primaryKey, modelClass);
	}

	/**
	 * Please use addMapping method making code shorter.
	 */
	@Deprecated
	public ActiveRecordPlugin addTableMapping(String tableName, Class<? extends Model<?>> modelClass) {
		return addMapping(tableName, modelClass);
	}

	public boolean start() {
		if (dataSourceProvider != null) {
			dataSource = dataSourceProvider.getDataSource();
		}

		if (dataSource == null) {
			throw new RuntimeException("ActiveRecord start error: ActiveRecordPlugin need DataSource or DataSourceProvider");
		}

		DbKit.setDataSource(dataSource);

		return TableInfo.buildTableInfo(tableMappings);
	}

	public boolean stop() {
		return true;
	}
}
