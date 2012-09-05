package net.jfly.plugin.ActiveRecord.dialect;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import net.jfly.plugin.ActiveRecord.Record;
import net.jfly.plugin.ActiveRecord.TableInfo;

public class MysqlDialect extends Dialect {

	@Override
	public String forTableInfoBuilderDoBuildTableInfo(String tableName) {
		return "select * from `" + tableName + "` limit 0";
	}

	public void forModelSave(TableInfo tableInfo, Map<String, Object> attrs, StringBuilder sql, List<Object> paras) {
		sql.append("insert into `").append(tableInfo.getTableName()).append("`(");
		StringBuilder temp = new StringBuilder(") values(");
		for (Entry<String, Object> e : attrs.entrySet()) {
			String colName = e.getKey();
			if (tableInfo.containsColumnName(colName)) {
				if (paras.size() > 0) {
					sql.append(", ");
					temp.append(", ");
				}
				sql.append("`").append(colName).append("`");
				temp.append("?");
				paras.add(e.getValue());
			}
		}
		sql.append(temp.toString()).append(")");
	}

	public String forModelDeleteById(TableInfo tInfo) {
		String primaryKey = tInfo.getPrimaryKey();
		StringBuilder sql = new StringBuilder(45);
		sql.append("delete from `");
		sql.append(tInfo.getTableName());
		sql.append("` where `").append(primaryKey).append("` = ?");
		return sql.toString();
	}

	public void forModelUpdate(TableInfo tableInfo, Map<String, Object> attrs, Set<String> modifyFlag, String primaryKey, Object id, StringBuilder sql, List<Object> paras) {
		sql.append("update `").append(tableInfo.getTableName()).append("` set ");
		for (Entry<String, Object> e : attrs.entrySet()) {
			String colName = e.getKey();
			if (!primaryKey.equalsIgnoreCase(colName) && modifyFlag.contains(colName) && tableInfo.containsColumnName(colName)) {
				if (paras.size() > 0)
					sql.append(", ");
				sql.append("`").append(colName).append("` = ? ");
				paras.add(e.getValue());
			}
		}
		sql.append(" where `").append(primaryKey).append("` = ?"); // .append(" limit 1");
		paras.add(id);
	}

	public String forModelFindById(TableInfo tInfo, String columns) {
		StringBuilder sql = new StringBuilder("select ");
		if (columns.trim().equals("*")) {
			sql.append(columns);
		} else {
			String[] columnsArray = columns.split(",");
			for (int i = 0; i < columnsArray.length; i++) {
				if (i > 0)
					sql.append(", ");
				sql.append("`");
				sql.append(columnsArray[i].trim());
				sql.append("`");
			}
		}
		sql.append(" from `");
		sql.append(tInfo.getTableName());
		sql.append("` where `").append(tInfo.getPrimaryKey()).append("` = ?");
		return sql.toString();
	}

	@Override
	public String forDbFindById(String tableName, String primaryKey, String columns) {
		StringBuilder sql = new StringBuilder("select ");
		if (columns.trim().equals("*")) {
			sql.append(columns);
		} else {
			String[] columnsArray = columns.split(",");
			for (int i = 0; i < columnsArray.length; i++) {
				if (i > 0)
					sql.append(", ");
				sql.append("`").append(columnsArray[i].trim()).append("`");
			}
		}
		sql.append(" from `");
		sql.append(tableName.trim());
		sql.append("` where `").append(primaryKey).append("` = ?");
		return sql.toString();
	}

	@Override
	public String forDbDeleteById(String tableName, String primaryKey) {
		StringBuilder sql = new StringBuilder("delete from `");
		sql.append(tableName.trim());
		sql.append("` where `").append(primaryKey).append("` = ?");
		return sql.toString();
	}

	public void forDbSave(StringBuilder sql, List<Object> paras, String tableName, Record record) {
		sql.append("insert into `");
		sql.append(tableName.trim()).append("`(");
		StringBuilder temp = new StringBuilder();
		temp.append(") values(");

		for (Entry<String, Object> e : record.getColumnMap().entrySet()) {
			if (paras.size() > 0) {
				sql.append(", ");
				temp.append(", ");
			}
			sql.append("`").append(e.getKey()).append("`");
			temp.append("?");
			paras.add(e.getValue());
		}
		sql.append(temp.toString()).append(")");
	}

	public void forDbUpdate(String tableName, String primaryKey, Object id, Record record, StringBuilder sql, List<Object> paras) {
		sql.append("update `").append(tableName.trim()).append("` set ");
		for (Entry<String, Object> e : record.getColumnMap().entrySet()) {
			String colName = e.getKey();
			if (!primaryKey.equalsIgnoreCase(colName)) {
				if (paras.size() > 0) {
					sql.append(", ");
				}
				sql.append("`").append(colName).append("` = ? ");
				paras.add(e.getValue());
			}
		}
		sql.append(" where `").append(primaryKey).append("` = ?"); // .append(" limit 1");
		paras.add(id);
	}

	@Override
	public void forPaginate(StringBuilder sql, int pageNumber, int pageSize, String select, String sqlExceptSelect) {
		int offset = pageSize * (pageNumber - 1);
		sql.append(select).append(" ");
		sql.append(sqlExceptSelect);
		sql.append(" limit ").append(offset).append(", ").append(pageSize); // limit
																			// can
																			// use
																			// one
																			// or
																			// two
																			// '?'
																			// to
																			// pass
																			// paras
	}

	@Override
	public boolean isSupportAutoIncrementKey() {
		return true;
	}

}