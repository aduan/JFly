package net.jfly.plugin.ActiveRecord;

import java.util.HashMap;
import java.util.Map;

/**
 * 本类记录表字段名及其类型
 */
public class TableInfo {

	private String tableName;
	// 默认值是ID
	private String primaryKey;
	private Class<? extends Model<?>> modelClass;
	private Map<String, Class<?>> columnTypeMap = new HashMap<String, Class<?>>();

	// 构造方法
	public TableInfo(String tableName, String primaryKey, Class<? extends Model<?>> modelClass) {
		if (tableName == null || "".equals(tableName.trim())) {
			throw new IllegalArgumentException("表名为空");
		}
		if (primaryKey == null || "".equals(primaryKey.trim())) {
			throw new IllegalArgumentException("主键为空");
		}
		if (modelClass == null) {
			throw new IllegalArgumentException("Model类为空");
		}
		this.tableName = tableName.trim();
		this.primaryKey = primaryKey.trim();
		this.modelClass = modelClass;
	}

	public TableInfo(String tableName, Class<? extends Model<?>> modelClass) {
		this(tableName, "id", modelClass);
	}

	public String getTableName() {
		return tableName;
	}

	public String getPrimaryKey() {
		return primaryKey;
	}

	public Class<? extends Model<?>> getModelClass() {
		return modelClass;
	}

	// 字段信息管理
	public void addColumnInfo(String columnName, Class<?> columnType) {
		columnTypeMap.put(columnName, columnType);
	}

	public Class<?> getColumnType(String columnName) {
		return columnTypeMap.get(columnName);
	}

	public boolean containsColumnName(String columnLabel) {
		return columnTypeMap.containsKey(columnLabel);
	}
}
