package net.jfly.plugin.ActiveRecord;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import net.jfly.core.Config;
import net.jfly.plugin.ActiveRecord.dialect.Dialect;
import net.jfly.plugin.ActiveRecord.dialect.MysqlDialect;
import net.jfly.util.TipSManager;

//final修饰的类不能被继承
public final class DbKit {
	private static final ThreadLocal<Connection> threadLocalConnection = new ThreadLocal<Connection>();
	static final Object[] NULL_PARA_ARRAY = new Object[0];
	private static int transactionLevel = Connection.TRANSACTION_READ_COMMITTED;
	public static boolean devMode = Config.getConstants().isDevMode();

	// 数据库处理支持：默认采用Mysql方言处理.可以在此处修改
	public static Dialect dialect = new MysqlDialect();

	public static void setDialect(Dialect dialect) {
		DbKit.dialect = dialect;
	}

	public static Dialect getDialect() {
		return dialect;
	}

	private static DataSource dataSource;

	private DbKit() {
	}

	public static final void setDataSource(DataSource dataSource) {
		DbKit.dataSource = dataSource;
	}

	public static final DataSource getDataSource() {
		return dataSource;
	}

	public static final void setTransactionLevel(int transactionLevel) {
		DbKit.transactionLevel = transactionLevel;
	}

	public static final int getTransactionLevel() {
		return transactionLevel;
	}

	// 取得连接的两种方式
	public static final Connection getConnection() throws SQLException {
		Connection conn = threadLocalConnection.get();
		// 线程共享变量
		if (conn != null) {
			return conn;
		}
		if (conn == null) {
			conn = dataSource.getConnection();
		}
		if (conn == null) {
			throw new RuntimeException("不能从数据源取得连接");
		} else {
			return conn;

		}
	}

	public static final void setThreadLocalConnection(Connection connection) {
		threadLocalConnection.set(connection);
	}

	public static final Connection getConnectionToThreadLocal() throws SQLException {
		Connection conn = threadLocalConnection.get();
		// 线程共享变量
		if (conn != null) {
			return conn;
		}
		if (conn == null) {
			conn = dataSource.getConnection();
		}
		if (conn == null) {
			throw new RuntimeException("不能从数据源取得连接");
		} else {
			threadLocalConnection.set(conn);
			return conn;

		}
	}

	// 从ThreadLocalConnection删除Connection

	public static final void removeThreadLocalConnection() throws SQLException {
		if (threadLocalConnection.get() != null) {
			threadLocalConnection.remove();
		}
	}

	// --------------------------------处理带有threadLocalConnection的资源关闭:threadLocalConnection如果为空则将Connection关闭--------------------------------
	public static final boolean isExistsThreadLocalConnection() {
		return threadLocalConnection.get() != null;
	}

	public static final void close(ResultSet rs, Statement stmt, Connection conn) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				TipSManager.showAndThrowErrorInfo(e);
			} finally {
				rs = null;
			}
		}
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				TipSManager.showAndThrowErrorInfo(e);
			} finally {
				stmt = null;
			}
		}
		if (threadLocalConnection.get() == null) {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					// [优化]如果连接出现异常必须向上抛出,ResultSet和Statement出现异常不要任意抛出异常,因为我们要确保Connection实例关闭.
					throw new ActiveRecordException(e);
				} finally {
					conn = null;
				}
			}
		}
	}

	public static final void close(Statement stmt, Connection conn) {
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				TipSManager.showAndThrowErrorInfo(e);

			} finally {
				stmt = null;
			}
		}

		if (threadLocalConnection.get() == null) {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					throw new ActiveRecordException(e);
				} finally {
					conn = null;
				}
			}
		}
	}

	public static final void close(Connection conn) {
		if (threadLocalConnection.get() == null)
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					throw new ActiveRecordException(e);
				} finally {
					conn = null;
				}
	}

	// --------------------------------处理忽略threadLocalConnection的资源关闭--------------------------------

	public static final void closeIgnoreThreadLocalConnection(ResultSet rs, Statement stmt, Connection conn) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				TipSManager.showAndThrowErrorInfo(e);
			} finally {
				rs = null;
			}
		}
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				TipSManager.showAndThrowErrorInfo(e);
			} finally {
				stmt = null;
			}
		}
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				// [优化]如果连接出现异常必须向上抛出,ResultSet和Statement出现异常不要任意抛出异常,因为我们要确保Connection实例关闭.
				throw new ActiveRecordException(e);
			} finally {
				conn = null;
			}
		}
	}

	public static final void closeIgnoreThreadLocalConnection(Statement stmt, Connection conn) {
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				TipSManager.showAndThrowErrorInfo(e);

			} finally {
				stmt = null;
			}
		}

		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				throw new ActiveRecordException(e);
			} finally {
				conn = null;
			}
		}
	}

	public static final void closeIgnoreThreadLocalConnection(Connection conn) {
		if (conn != null)
			try {
				conn.close();
			} catch (SQLException e) {
				throw new ActiveRecordException(e);
			} finally {
				conn = null;
			}
	}

	// 单独
	public static final void closeYe(ResultSet rs, Statement stmt) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				TipSManager.showAndThrowErrorInfo(e);
			} finally {
				stmt = null;
			}
		}
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				TipSManager.showAndThrowErrorInfo(e);
			} finally {
				stmt = null;
			}
		}
	}

	public static final void closeYe(Statement stmt) {
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				TipSManager.showAndThrowErrorInfo(e);
			} finally {
				stmt = null;
			}
		}
	}

	public static final void closeYe(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				throw new ActiveRecordException(e);
			} finally {
				conn = null;
			}
		}
	}

	public static String replaceFormatSqlOrderBy(String sql) {
		sql = sql.replaceAll("(\\s)+", " ");
		int index = sql.toLowerCase().lastIndexOf("order by");
		if (index > sql.toLowerCase().lastIndexOf(")")) {
			String sql1 = sql.substring(0, index);
			String sql2 = sql.substring(index);
			sql2 = sql2.replaceAll("[oO][rR][dD][eE][rR] [bB][yY] [a-zA-Z0-9_.]+((\\s)+(([dD][eE][sS][cC])|([aA][sS][cC])))?(( )*,( )*[a-zA-Z0-9_.]+(( )+(([dD][eE][sS][cC])|([aA][sS][cC])))?)*", "");
			return sql1 + sql2;
		}
		return sql;
	}

}
