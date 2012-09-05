package net.jfly.plugin.ActiveRecord;

import java.sql.SQLException;

/**
 * Database transaction atomic:数据库事务原子
 */
public interface IAtomic {
	// 返回真则提交事务,为假则回滚
	boolean pransactionProcessing() throws SQLException;
}