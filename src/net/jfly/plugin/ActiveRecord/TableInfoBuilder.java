package net.jfly.plugin.ActiveRecord;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.List;

class ModelAndTableInfoBuilder {

	static boolean buildTableInfo(List<TableInfo> tableInfoList) {
		boolean succeed = true;
		Connection conn = null;
		try {
			conn = DbKit.getDataSource().getConnection();
		} catch (SQLException e) {
			throw new ActiveRecordException(e);
		}

		ModelAndTableInfoMapping modelAndTableInfoMapping = ModelAndTableInfoMapping.getInstance();
		for (TableInfo aTableInfo : tableInfoList) {
			try {
				TableInfo tableInfo = doBuildTableInfo(aTableInfo, conn);
				modelAndTableInfoMapping.addModelAndTableInfoMapping(aTableInfo.getModelClass(), tableInfo);
			} catch (Exception e) {
				succeed = false;
				System.err.println("不能为" + aTableInfo.getTableName() + "表构建映射关系" + e);
				throw new ActiveRecordException(e);
			}
		}
		DbKit.close(conn);
		return succeed;
	}

	private static TableInfo doBuildTableInfo(TableInfo tableInfo, Connection conn) throws SQLException {
		TableInfo result = tableInfo;

		String sql = DbKit.getDialect().forTableInfoBuilderDoBuildTableInfo(tableInfo.getTableName());
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		ResultSetMetaData rsmd = rs.getMetaData();

		for (int i = 1; i <= rsmd.getColumnCount(); i++) {
			String colName = rsmd.getColumnName(i);
			// ~1直接通过数据库ResultSetMetaData.getColumnClassName来查找字段类型
			String colClassName = rsmd.getColumnClassName(i);
			// 原来是用[if-else]结构,效率很高.但为了好看,故使用[continue]结构
			if ("java.lang.String".equals(colClassName)) {
				result.addColumnInfo(colName, java.lang.String.class);
				continue;
			}
			if ("java.lang.Boolean".equals(colClassName)) {
				result.addColumnInfo(colName, java.lang.Boolean.class);
				continue;
			}
			if ("[B".equals(colClassName)) {
				result.addColumnInfo(colName, byte[].class);
				continue;
			}
			if ("java.lang.Integer".equals(colClassName)) {
				result.addColumnInfo(colName, java.lang.Integer.class);
				continue;
			}
			if ("java.lang.Long".equals(colClassName)) {
				result.addColumnInfo(colName, java.lang.Long.class);
				continue;
			}
			if ("java.lang.Float".equals(colClassName)) {
				result.addColumnInfo(colName, java.lang.Float.class);
				continue;
			}
			if ("java.lang.Double".equals(colClassName)) {
				result.addColumnInfo(colName, java.lang.Double.class);
				continue;
			}
			if ("java.math.BigDecimal".equals(colClassName)) {
				result.addColumnInfo(colName, java.math.BigDecimal.class);
				continue;
			}

			if ("java.sql.Date".equals(colClassName)) {
				result.addColumnInfo(colName, java.sql.Date.class);
				continue;
			}
			if ("java.sql.Time".equals(colClassName)) {
				result.addColumnInfo(colName, java.sql.Time.class);
				continue;
			}
			if ("java.sql.Timestamp".equals(colClassName)) {
				result.addColumnInfo(colName, java.sql.Timestamp.class);
				continue;
			}
			// ~2直接通过数据库ResultSetMetaData.getColumnType来查找字段类型
			else {
				int type = rsmd.getColumnType(i);
				// byte
				if (type == Types.BLOB) {
					result.addColumnInfo(colName, byte[].class);
					// char
				} else if (type == Types.CLOB || type == Types.NCLOB) {
					result.addColumnInfo(colName, String.class);
				} else {
					result.addColumnInfo(colName, String.class);
				}
			}
		}

		rs.close();
		stmt.close();
		return result;
	}
}
