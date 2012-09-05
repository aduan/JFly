package net.jfly.plugin.ActiveRecord.dialect;

import java.util.List;
import java.util.Map;
import java.util.Set;

import net.jfly.plugin.ActiveRecord.Record;
import net.jfly.plugin.ActiveRecord.TableInfo;

public abstract class Dialect {
	public abstract String forTableInfoBuilderDoBuildTableInfo(String tableName);

	public abstract void forModelSave(TableInfo tableInfo, Map<String, Object> attrs, StringBuilder sql, List<Object> paras);

	public abstract String forModelDeleteById(TableInfo tInfo);

	public abstract void forModelUpdate(TableInfo tableInfo, Map<String, Object> attrs, Set<String> modifyFlag, String pKey, Object id, StringBuilder sql, List<Object> paras);

	public abstract String forModelFindById(TableInfo tInfo, String columns);

	public abstract String forDbFindById(String tableName, String primaryKey, String columns);

	public abstract String forDbDeleteById(String tableName, String primaryKey);

	public abstract void forDbSave(StringBuilder sql, List<Object> paras, String tableName, Record record);

	public abstract void forDbUpdate(String tableName, String primaryKey, Object id, Record record, StringBuilder sql, List<Object> paras);

	public abstract void forPaginate(StringBuilder sql, int pageNumber, int pageSize, String select, String sqlExceptSelect);

	public abstract boolean isSupportAutoIncrementKey();
}
