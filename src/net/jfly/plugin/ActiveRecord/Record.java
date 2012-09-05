package net.jfly.plugin.ActiveRecord;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class Record implements Serializable {

	private static final long serialVersionUID = -3254070837297655225L;
	private Map<String, Object> columnMap = new HashMap<String, Object>();

	// 添加字段
	public Record add(String column, Object value) {
		columnMap.put(column, value);
		return this;
	}

	public Record addColumnMap(Map<String, Object> columnMap) {
		this.columnMap.putAll(columnMap);
		return this;
	}

	public Record addColumnMap(Record record) {
		columnMap.putAll(record.getColumnMap());
		return this;
	}

	// 删除字段
	public Record remove(String column) {
		columnMap.remove(column);
		return this;
	}

	public Record removeNullValueColumns() {
		for (Iterator<Entry<String, Object>> iterator = columnMap.entrySet().iterator(); iterator.hasNext();) {
			Entry<String, Object> entry = iterator.next();
			if (entry.getValue() == null) {
				iterator.remove();
			}
		}
		return this;
	}

	public Record remove(String... columns) {
		if (columns != null) {
			for (String column : columns) {
				this.columnMap.remove(column);
			}

		}
		return this;
	}

	public Record keep(String column) {
		if (columnMap.containsKey(column)) {
			Object keepIt = columnMap.get(column);
			columnMap.clear();
			columnMap.put(column, keepIt);
		} else {
			columnMap.clear();
		}
		return this;
	}

	public Record keep(String... columns) {
		if (columns != null && columns.length > 0) {
			Map<String, Object> newColumns = new HashMap<String, Object>(columns.length);
			for (String column : columns) {
				if (this.columnMap.containsKey(column)) {
					newColumns.put(column, this.columnMap.get(column));
				}
			}
			this.columnMap = newColumns;
		} else
			this.columnMap.clear();
		return this;
	}

	public Record clear() {
		columnMap.clear();
		return this;
	}

	// 获取字段
	public Map<String, Object> getColumnMap() {
		return columnMap;
	}

	@SuppressWarnings("unchecked")
	public <T> T get(String column) {
		return (T) columnMap.get(column);
	}

	@SuppressWarnings("unchecked")
	public <T> T get(String column, Object defaultValue) {
		Object result = columnMap.get(column);
		return (T) (result != null ? result : defaultValue);
	}

	/**
	 * <pre>
	 * 针对数据库类型: varchar, char, enum, set, text, tinytext, mediumtext, longtext
	 * </pre>
	 */
	public String getString(String column) {
		return (String) columnMap.get(column);
	}

	/**
	 * <pre>
	 * 针对数据库类型: binary, varbinary, tinyblob, blob, mediumblob,longblob.
	 * </pre>
	 */
	public byte[] getBytes(String column) {
		return (byte[]) columnMap.get(column);
	}

	/**
	 * <pre>
	 * 针对数据库类型: bit, tinyint(1)
	 * </pre>
	 */
	public Boolean getBoolean(String column) {
		return (Boolean) columnMap.get(column);
	}

	// ~1 数字类型
	public Number getNumber(String column) {
		return (Number) columnMap.get(column);
	}

	/**
	 * <pre>
	 * 针对数据库类型:int, integer, tinyint(n) n > 1, smallint,mediumint
	 * </pre>
	 */
	public Integer getInt(String column) {
		return (Integer) columnMap.get(column);
	}

	/**
	 * <pre>
	 * 针对数据库类型: bigint
	 * </pre>
	 */
	public Long getLong(String column) {
		return (Long) columnMap.get(column);
	}

	/**
	 * <pre>
	 * 针对数据库类型: float
	 * </pre>
	 */
	public Float getFloat(String column) {
		return (Float) columnMap.get(column);
	}

	/**
	 * <pre>
	 * 针对数据库类型: real, double
	 * </pre>
	 */
	public Double getDouble(String column) {
		return (Double) columnMap.get(column);
	}

	/**
	 * <pre>
	 * 针对数据库类型: decimal, numeric
	 * </pre>
	 */
	public java.math.BigDecimal getBigDecimal(String column) {
		return (java.math.BigDecimal) columnMap.get(column);
	}

	// ~2日期类型

	/**
	 * <pre>
	 * 针对数据库类型: date, year
	 * </pre>
	 */
	public java.sql.Date getDate(String column) {
		return (java.sql.Date) columnMap.get(column);
	}

	/**
	 * <pre>
	 * 针对数据库类型: time
	 * </pre>
	 */
	public java.sql.Time getTime(String column) {
		return (java.sql.Time) columnMap.get(column);
	}

	/**
	 * <pre>
	 * 针对数据库类型: timestamp, datetime
	 * </pre>
	 */
	public java.sql.Timestamp getTimestamp(String column) {
		return (java.sql.Timestamp) columnMap.get(column);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(super.toString()).append(" {");
		boolean flag = false;
		for (Entry<String, Object> e : columnMap.entrySet()) {
			if (flag == false) {
				flag = true;
			} else {
				sb.append("    ,    ");
			}
			Object value = e.getValue();
			if (value != null) {
				value = value.toString();
			}
			sb.append(e.getKey()).append(":").append(value);
		}
		sb.append("}");
		return sb.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Record)) {
			return false;
		}
		if (o == this) {
			return true;
		}
		// 比较map是否相等
		return this.columnMap.equals(((Record) o).columnMap);
	}

	@Override
	public int hashCode() {
		return columnMap == null ? 0 : columnMap.hashCode();
	}
}
