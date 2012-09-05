package net.jfly.plugin.ActiveRecord;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

/**
 * 强大的数据库查询工具类
 */
@SuppressWarnings( { "rawtypes", "unchecked" })
public class Db {

	private static Object[] Null_Object_Array = new Object[0];

	// 查询
	public static <T> List<T> query(Connection conn, String sql, Object... params) throws SQLException {
		List result = new ArrayList();
		PreparedStatement pstmt = conn.prepareStatement(sql);

		for (int i = 0; i < params.length; i++) {
			pstmt.setObject(i + 1, params[i]);
		}
		ResultSet rs = pstmt.executeQuery();

		int colAmount = rs.getMetaData().getColumnCount();
		if (colAmount > 1) {
			while (rs.next()) {
				Object[] temp = new Object[colAmount];
				for (int i = 0; i < colAmount; i++) {
					temp[i] = rs.getObject(i + 1);
				}
				result.add(temp);
			}
		} else if (colAmount == 1) {
			while (rs.next()) {
				result.add(rs.getObject(1));
			}
		}
		DbKit.closeYe(rs, pstmt);

		return result;
	}

	public static <T> List<T> query(String sql, Object... params) {
		Connection conn = null;
		try {
			conn = DbKit.getConnection();
			return query(conn, sql, params);
		} catch (Exception e) {
			throw new ActiveRecordException(e);
		} finally {
			DbKit.close(conn);
		}
	}

	public static <T> List<T> query(DataSource dataSource, String sql, Object... params) {
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			return query(conn, sql, params);
		} catch (Exception e) {
			throw new ActiveRecordException(e);
		} finally {
			DbKit.closeYe(conn);
		}
	}

	public static <T> List<T> query(DataSource dataSource, String sql) {
		return query(dataSource, sql, Null_Object_Array);
	}

	public static <T> List<T> query(String sql) { // return List<object[]> or
		// List<object>
		return query(sql, Null_Object_Array);
	}

	/**
	 * Execute sql query and return the first result. I recommend add "limit 1"
	 * in your sql.
	 */
	public static <T> T queryFirst(String sql, Object... paras) {
		List<T> result = query(sql, paras);
		return (result.size() > 0 ? result.get(0) : null);
	}

	public static <T> T queryFirst(String sql) {
		List<T> result = query(sql, Null_Object_Array);
		return (result.size() > 0 ? result.get(0) : null);
	}

	/**
	 * Execute sql query just return one column.
	 */
	public static <T> T queryColumn(String sql, Object... params) {
		List<T> result = query(sql, params);
		if (result.size() > 0) {
			T temp = result.get(0);
			if (temp instanceof Object[])
				throw new ActiveRecordException("Only ONE COLUMN can be queried.");
			return temp;
		}
		return null;
	}

	public static <T> T queryColumn(String sql) {
		return queryColumn(sql, Null_Object_Array);
	}

	public static String queryStr(String sql, Object... paras) {
		return (String) queryColumn(sql, paras);
	}

	public static String queryStr(String sql) {
		return (String) queryColumn(sql, Null_Object_Array);
	}

	public static Integer queryInt(String sql, Object... paras) {
		return (Integer) queryColumn(sql, paras);
	}

	public static Integer queryInt(String sql) {
		return (Integer) queryColumn(sql, Null_Object_Array);
	}

	public static Long queryLong(String sql, Object... paras) {
		return (Long) queryColumn(sql, paras);
	}

	public static Long queryLong(String sql) {
		return (Long) queryColumn(sql, Null_Object_Array);
	}

	public static Double queryDouble(String sql, Object... paras) {
		return (Double) queryColumn(sql, paras);
	}

	public static Double queryDouble(String sql) {
		return (Double) queryColumn(sql, Null_Object_Array);
	}

	public static Float queryFloat(String sql, Object... paras) {
		return (Float) queryColumn(sql, paras);
	}

	public static Float queryFloat(String sql) {
		return (Float) queryColumn(sql, Null_Object_Array);
	}

	public static java.math.BigDecimal queryBigDecimal(String sql, Object... paras) {
		return (java.math.BigDecimal) queryColumn(sql, paras);
	}

	public static java.math.BigDecimal queryBigDecimal(String sql) {
		return (java.math.BigDecimal) queryColumn(sql, Null_Object_Array);
	}

	public static byte[] queryBytes(String sql, Object... paras) {
		return (byte[]) queryColumn(sql, paras);
	}

	public static byte[] queryBytes(String sql) {
		return (byte[]) queryColumn(sql, Null_Object_Array);
	}

	public static java.sql.Date queryDate(String sql, Object... paras) {
		return (java.sql.Date) queryColumn(sql, paras);
	}

	public static java.sql.Date queryDate(String sql) {
		return (java.sql.Date) queryColumn(sql, Null_Object_Array);
	}

	public static java.sql.Time queryTime(String sql, Object... paras) {
		return (java.sql.Time) queryColumn(sql, paras);
	}

	public static java.sql.Time queryTime(String sql) {
		return (java.sql.Time) queryColumn(sql, Null_Object_Array);
	}

	public static java.sql.Timestamp queryTimestamp(String sql, Object... paras) {
		return (java.sql.Timestamp) queryColumn(sql, paras);
	}

	public static java.sql.Timestamp queryTimestamp(String sql) {
		return (java.sql.Timestamp) queryColumn(sql, Null_Object_Array);
	}

	public static Boolean queryBoolean(String sql, Object... paras) {
		return (Boolean) queryColumn(sql, paras);
	}

	public static Boolean queryBoolean(String sql) {
		return (Boolean) queryColumn(sql, Null_Object_Array);
	}

	public static Number queryNumber(String sql, Object... paras) {
		return (Number) queryColumn(sql, paras);
	}

	public static Number queryNumber(String sql) {
		return (Number) queryColumn(sql, Null_Object_Array);
	}

	// 更新
	public static int update(Connection conn, String sql, Object... paras) throws SQLException {
		PreparedStatement pstmt = conn.prepareStatement(sql);
		for (int i = 0; i < paras.length; i++) {
			pstmt.setObject(i + 1, paras[i]);
		}
		int result = pstmt.executeUpdate();
		DbKit.closeYe(pstmt);

		return result;
	}

	public static int update(String sql, Object... paras) {
		Connection conn = null;
		try {
			conn = DbKit.getConnection();
			return update(conn, sql, paras);
		} catch (Exception e) {
			throw new ActiveRecordException(e);
		} finally {
			DbKit.close(conn);
		}
	}

	public static int update(DataSource dataSource, String sql, Object... paras) {
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			return update(conn, sql, paras);
		} catch (Exception e) {
			throw new ActiveRecordException(e);
		} finally {
			DbKit.closeYe(conn);
		}
	}

	public static int update(DataSource dataSource, String sql) {
		return update(dataSource, sql, Null_Object_Array);
	}

	public static int update(String sql) {
		return update(sql, Null_Object_Array);
	}

	private static Object getGeneratedKey(PreparedStatement pst) throws SQLException {
		ResultSet rs = pst.getGeneratedKeys();
		Object id = null;
		if (rs.next())
			id = rs.getObject(1);
		rs.close();
		return id;
	}

	public static List<Record> find(Connection conn, String sql, Object... paras) throws SQLException {
		PreparedStatement pst = conn.prepareStatement(sql);
		for (int i = 0; i < paras.length; i++) {
			pst.setObject(i + 1, paras[i]);
		}
		ResultSet rs = pst.executeQuery();
		List<Record> result = RecordBuilder.build(rs);
		DbKit.closeYe(rs, pst);
		return result;
	}

	public static List<Record> find(String sql, Object... paras) {
		Connection conn = null;
		try {
			conn = DbKit.getConnection();
			return find(conn, sql, paras);
		} catch (Exception e) {
			throw new ActiveRecordException(e);
		} finally {
			DbKit.close(conn);
		}
	}

	public static List<Record> find(String sql) {
		return find(sql, Null_Object_Array);
	}

	public static List<Record> find(DataSource dataSource, String sql, Object... paras) {
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			return find(conn, sql, paras);
		} catch (Exception e) {
			throw new ActiveRecordException(e);
		} finally {
			DbKit.closeYe(conn);
		}
	}

	public static List<Record> find(DataSource dataSource, String sql) {
		return find(dataSource, sql, Null_Object_Array);
	}

	/**
	 * Find first record. I recommend add "limit 1" in your sql.
	 */
	public static Record findFirst(String sql, Object... paras) {
		List<Record> result = find(sql, paras);
		return result.size() > 0 ? result.get(0) : null;
	}

	/**
	 * @see #findFirst(String, Object...)
	 * @param sql
	 *            an SQL statement
	 */
	public static Record findFirst(String sql) {
		List<Record> result = find(sql, Null_Object_Array);
		return result.size() > 0 ? result.get(0) : null;
	}

	/**
	 * Find record by id. Example: Record user = Db.findById("user", 15);
	 * 
	 * @param tableName
	 *            the table name of the table
	 * @param id
	 *            the id value of the record
	 */
	public static Record findById(String tableName, Object id) {
		return findById(tableName, "id", id, "*");
	}

	/**
	 * Find record by id. Fetch the specific columns only. Example: Record user
	 * = Db.findById("user", 15, "name, age");
	 * 
	 * @param tableName
	 *            the table name of the table
	 * @param id
	 *            the id value of the record
	 * @param columns
	 *            the specific columns separate with comma character ==> ","
	 */
	public static Record findById(String tableName, Object id, String columns) {
		return findById(tableName, "id", id, columns);
	}

	/**
	 * Find record by id. Example: Record user = Db.findById("user",
	 * "user_id",15);
	 */
	public static Record findById(String tableName, String primaryKey, Object id) {
		return findById(tableName, primaryKey, id, "*");
	}

	/**
	 * Find record by id. Fetch the specific columns only. Example: Record user=
	 * Db.findById("user", "user_id", 15, "name, age");
	 */
	public static Record findById(String tableName, String primaryKey, Object id, String columns) {
		String sql = DbKit.getDialect().forDbFindById(tableName, primaryKey, columns);
		List<Record> result = find(sql, id);
		return result.size() > 0 ? result.get(0) : null;
	}

	/**
	 * Delete record by id. Example: boolean succeed = Db.deleteById("user",
	 * 15);
	 */
	public static boolean deleteById(String tableName, Object id) {
		return deleteById(tableName, "id", id);
	}

	/**
	 * Delete record by id. Example: boolean succeed = Db.deleteById("user",
	 * "user_id", 15);
	 */
	public static boolean deleteById(String tableName, String primaryKey, Object id) {
		if (id == null)
			throw new IllegalArgumentException("id can not be null");

		String sql = DbKit.getDialect().forDbDeleteById(tableName, primaryKey);
		return update(sql, id) >= 1;
	}

	/**
	 * Delete record. Example: boolean succeed = Db.delete("user", "id", user);
	 * 
	 * @param tableName
	 *            the table name of the table
	 * @param primaryKey
	 *            the primary key of the table
	 * @param record
	 *            the record
	 * @return true if delete succeed otherwise false
	 */
	public static boolean delete(String tableName, String primaryKey, Record record) {
		return deleteById(tableName, primaryKey, record.get(primaryKey));
	}

	/**
	 * Example: boolean succeed = Db.delete("user", user);
	 * 
	 * @see #delete(String, String, Record)
	 */
	public static boolean delete(String tableName, Record record) {
		return deleteById(tableName, "id", record.get("id"));
	}

	public static boolean save(Connection conn, String tableName, String primaryKey, Record record) throws SQLException {
		List<Object> paras = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder();
		DbKit.getDialect().forDbSave(sql, paras, tableName, record);

		PreparedStatement pst;
		boolean isSupportAutoIncrementKey = DbKit.getDialect().isSupportAutoIncrementKey();
		if (isSupportAutoIncrementKey)
			pst = conn.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
		else
			pst = conn.prepareStatement(sql.toString());
		for (int i = 0, size = paras.size(); i < size; i++) {
			pst.setObject(i + 1, paras.get(i));
		}
		int result = pst.executeUpdate();
		if (isSupportAutoIncrementKey)
			record.add(primaryKey, getGeneratedKey(pst));
		DbKit.closeYe(pst);

		return result >= 1;
	}

	/**
	 * @see #save(DataSource, String, String, Record)
	 */
	public static boolean save(String tableName, String primaryKey, Record record) {
		Connection conn = null;
		try {
			conn = DbKit.getConnection();
			return save(conn, tableName, primaryKey, record);
		} catch (Exception e) {
			throw new ActiveRecordException(e);
		} finally {
			DbKit.close(conn);
		}
	}

	/**
	 * @see #save(DataSource, String, String, Record)
	 */
	public static boolean save(String tableName, Record record) {
		return save(tableName, "id", record);
	}

	/**
	 * Save record.
	 * 
	 * @param dataSource
	 *            the DataSource for this query
	 * @param tableName
	 *            the table name of the table
	 * @param primaryKey
	 *            the primary key of the table
	 * @param record
	 *            the record will be saved
	 * @param true if save succeed otherwise false
	 */
	public static boolean save(DataSource dataSource, String tableName, String primaryKey, Record record) {
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			return save(conn, tableName, primaryKey, record);
		} catch (Exception e) {
			throw new ActiveRecordException(e);
		} finally {
			DbKit.closeYe(conn);
		}
	}

	/**
	 * @see #save(DataSource, String, String, Record)
	 */
	public static boolean save(DataSource dataSource, String tableName, Record record) {
		return save(dataSource, tableName, "id", record);
	}

	static boolean update(Connection conn, String tableName, String primaryKey, Record record) throws SQLException {
		Object id = record.get(primaryKey);
		if (id == null)
			throw new ActiveRecordException("You can't update model whitout Primary Key.");

		StringBuilder sql = new StringBuilder();
		List<Object> paras = new ArrayList<Object>();
		DbKit.getDialect().forDbUpdate(tableName, primaryKey, id, record, sql, paras);

		if (paras.size() <= 1) { // Needn't update
			return false;
		}

		return update(conn, sql.toString(), paras.toArray()) >= 1; // > 0;
	}

	/**
	 * @see #update(DataSource, String, String, Record)
	 */
	public static boolean update(String tableName, String primaryKey, Record record) {
		Connection conn = null;
		try {
			conn = DbKit.getConnection();
			return update(conn, tableName, primaryKey, record);
		} catch (Exception e) {
			throw new ActiveRecordException(e);
		} finally {
			DbKit.close(conn);
		}
	}

	/**
	 * Update Record. The primary key of the table is: "id".
	 * 
	 * @see #update(DataSource, String, String, Record)
	 */
	public static boolean update(String tableName, Record record) {
		return update(tableName, "id", record);
	}

	/**
	 * Update Record.
	 * 
	 * @param dataSource
	 *            the DataSource for this query
	 * @param tableName
	 *            the table name of the Record save to
	 * @param primaryKey
	 *            the primary key of the table
	 * @param record
	 *            the Record object
	 * @param true if update succeed otherwise false
	 */
	public static boolean update(DataSource dataSource, String tableName, String primaryKey, Record record) {
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			return update(conn, tableName, primaryKey, record);
		} catch (Exception e) {
			throw new ActiveRecordException(e);
		} finally {
			DbKit.closeYe(conn);
		}
	}

	/**
	 * Update Record. The primary key of the table is: "id".
	 * 
	 * @see #update(DataSource, String, String, Record)
	 */
	public static boolean update(DataSource dataSource, String tableName, Record record) {
		return update(dataSource, tableName, "id", record);
	}

	/**
	 * @see #execute(DataSource, ICallback)
	 */
	public static void execute(ICallback callback) {
		execute(DbKit.getDataSource(), callback);
	}

	public static void execute(DataSource dataSource, ICallback callback) {
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			callback.run(conn);
		} catch (Exception e) {
			throw new ActiveRecordException(e);
		} finally {
			DbKit.closeYe(conn);
		}
	}

	public static boolean tx(int transactionLevel, IAtomic iAtomic) {
		if (DbKit.isExistsThreadLocalConnection())
			throw new ActiveRecordException("Nested transaction can not be supported. You can't execute transaction inside another transaction.");

		Connection conn = null;
		Boolean autoCommit = null;
		try {
			conn = DbKit.getDataSource().getConnection();
			autoCommit = conn.getAutoCommit();
			DbKit.setThreadLocalConnection(conn);
			conn.setTransactionIsolation(transactionLevel);
			conn.setAutoCommit(false);
			boolean result = iAtomic.pransactionProcessing();
			if (result)
				conn.commit();
			else
				conn.rollback();
			return result;
		} catch (Exception e) {
			if (conn != null)
				try {
					conn.rollback();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			throw new ActiveRecordException(e);
		} finally {
			try {
				if (conn != null) {
					if (autoCommit != null)
						conn.setAutoCommit(autoCommit);
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace(); // can not throw exception here, otherwise
				// the more important exception in
				// previous catch block can not be
				// thrown
			} finally {
				try {
					DbKit.removeThreadLocalConnection();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} // prevent memory leak
			}
		}
	}

	public static boolean tx(IAtomic atom) {
		// atom.pransactionProcessing();
		return tx(DbKit.getTransactionLevel(), atom);
	}

	private static int[] batch(Connection conn, String sql, Object[][] paras, int batchSize) throws SQLException {
		if (paras == null || paras.length == 0)
			throw new IllegalArgumentException("The paras array length must more than 0.");
		if (batchSize < 1)
			throw new IllegalArgumentException("The batchSize must more than 0.");
		int counter = 0;
		int pointer = 0;
		int[] result = new int[paras.length];
		PreparedStatement pst = conn.prepareStatement(sql);
		for (int i = 0; i < paras.length; i++) {
			for (int j = 0; j < paras[i].length; j++) {
				pst.setObject(j + 1, paras[i][j]);
			}
			pst.addBatch();
			if (++counter >= batchSize) {
				counter = 0;
				int[] r = pst.executeBatch();
				conn.commit();
				for (int k = 0; k < r.length; k++)
					result[pointer++] = r[k];
			}
		}
		int[] r = pst.executeBatch();
		conn.commit();
		for (int k = 0; k < r.length; k++)
			result[pointer++] = r[k];
		DbKit.closeYe(pst);
		return result;
	}

	/**
	 * Execute a batch of SQL INSERT, UPDATE, or DELETE queries.
	 * <p>
	 * Example:
	 * 
	 * <pre>
	 * String sql = &quot;insert into user(name, cash) values(?, ?)&quot;;
	 * int[] result = Db.batch(sql, new Object[][] { { &quot;James&quot;, 888 }, { &quot;zhanjin&quot;, 888 } });
	 * </pre>
	 */
	public static int[] batch(String sql, Object[][] paras, int batchSize) {
		Connection conn = null;
		Boolean autoCommit = null;
		try {
			conn = DbKit.getConnection();
			autoCommit = conn.getAutoCommit();
			conn.setAutoCommit(false);
			return batch(conn, sql, paras, batchSize);
		} catch (Exception e) {
			throw new ActiveRecordException(e);
		} finally {
			if (autoCommit != null)
				try {
					conn.setAutoCommit(autoCommit);
				} catch (Exception e) {
					e.printStackTrace();
				}
			DbKit.close(conn);
		}
	}

	public static int[] batch(DataSource dataSource, String sql, Object[][] paras, int batchSize) {
		Connection conn = null;
		Boolean autoCommit = null;
		try {
			conn = dataSource.getConnection();
			autoCommit = conn.getAutoCommit();
			conn.setAutoCommit(false);
			return batch(conn, sql, paras, batchSize);
		} catch (Exception e) {
			throw new ActiveRecordException(e);
		} finally {
			if (autoCommit != null)
				try {
					conn.setAutoCommit(autoCommit);
				} catch (Exception e) {
					e.printStackTrace();
				}
			DbKit.closeYe(conn);
		}
	}

	/**
	 * 批处理:都会调用这个方法.
	 * 
	 * <pre>
	 * columns里面是用','包含的字符数组的字符串.
	 * batchSize 表示每多少条数据提交一次
	 * </pre>
	 * 
	 */
	private static int[] batch(Connection conn, String sql, String columns, List list, int batchSize) throws SQLException {
		if (list == null || list.size() == 0) {
			return new int[0];
		}
		// 元素类型检查
		Object element = list.get(0);
		if (!(element instanceof Record) && !(element instanceof Model)) {
			throw new IllegalArgumentException("List中的元素必须是 Model或者 Record的实例");
		}

		if (batchSize < 1) {
			throw new IllegalArgumentException("批处理的大小必须大于0");
		}

		boolean isModel = element instanceof Model;

		String[] columnArray = columns.split(",");
		for (int i = 0; i < columnArray.length; i++) {
			columnArray[i] = columnArray[i].trim();
		}
		int counter = 0;
		int pointer = 0;
		int size = list.size();
		// 记录每条数据的执行情况
		int[] result = new int[size];
		PreparedStatement pstmt = conn.prepareStatement(sql);
		for (int i = 0; i < size; i++) {
			java.util.Map map = isModel ? ((Model) list.get(i)).getAttributeMap() : ((Record) list.get(i)).getColumnMap();
			for (int j = 0; j < columnArray.length; j++) {
				// 字段一一对应
				pstmt.setObject(j + 1, map.get(columnArray[j]));
			}
			pstmt.addBatch();
			{// 批处理
				if (++counter >= batchSize) {
					counter = 0;
					int[] r = pstmt.executeBatch();
					conn.commit();
					for (int j = 0; j < r.length; j++) {
						// 从数组下标元素0开始
						result[pointer++] = r[j];
					}
				}
			}
		}
		// 剩下的数据批处理
		int[] r = pstmt.executeBatch();
		conn.commit();
		for (int k = 0; k < r.length; k++) {
			result[pointer++] = r[k];
		}

		DbKit.closeYe(pstmt);
		return result;
	}

	/**
	 * Execute a batch of SQL INSERT, UPDATE, or DELETE queries.
	 * <p>
	 * Example:
	 * 
	 * <pre>
	 * String sql = &quot;insert into user(name, cash) values(?, ?)&quot;;
	 * int[] result = Db.batch(sql, &quot;name, cash&quot;, modelList, 500);
	 * </pre>
	 */
	public static int[] batch(String sql, String columns, List modelOrRecordList, int batchSize) {
		Connection conn = null;
		Boolean autoCommit = null;
		try {
			conn = DbKit.getConnection();
			autoCommit = conn.getAutoCommit();
			conn.setAutoCommit(false);
			return batch(conn, sql, columns, modelOrRecordList, batchSize);
		} catch (Exception e) {
			throw new ActiveRecordException(e);
		} finally {
			if (autoCommit != null)
				try {
					conn.setAutoCommit(autoCommit);
				} catch (Exception e) {
					e.printStackTrace();
				}
			DbKit.close(conn);
		}
	}

	public static int[] batch(DataSource dataSource, String sql, String columns, List modelOrRecordList, int batchSize) {
		Connection conn = null;
		Boolean autoCommit = null;
		try {
			conn = dataSource.getConnection();
			autoCommit = conn.getAutoCommit();
			conn.setAutoCommit(false);
			return batch(conn, sql, columns, modelOrRecordList, batchSize);
		} catch (Exception e) {
			throw new ActiveRecordException(e);
		} finally {
			if (autoCommit != null)
				try {
					conn.setAutoCommit(autoCommit);
				} catch (Exception e) {
					e.printStackTrace();
				}
			DbKit.closeYe(conn);
		}
	}

	/**
	 * 批处理.必须支持事物管理
	 */
	private static int[] batch(Connection conn, List<String> sqlList, int batchSize) throws SQLException {
		if (sqlList == null || sqlList.size() == 0) {
			throw new IllegalArgumentException("The sqlList length must more than 0.");
		}

		if (batchSize < 1) {
			throw new IllegalArgumentException("The batchSize must more than 0.");
		}

		int counter = 0;
		int pointer = 0;
		int size = sqlList.size();
		int[] result = new int[size];
		Statement st = conn.createStatement();
		for (int i = 0; i < size; i++) {
			st.addBatch(sqlList.get(i));
			if (++counter >= batchSize) {
				counter = 0;
				int[] r = st.executeBatch();
				conn.commit();
				for (int k = 0; k < r.length; k++)
					result[pointer++] = r[k];
			}
		}
		int[] r = st.executeBatch();
		conn.commit();
		for (int k = 0; k < r.length; k++)
			result[pointer++] = r[k];
		DbKit.closeYe(st);
		return result;
	}

	public static int[] batch(List<String> sqlList, int batchSize) {
		Connection conn = null;
		Boolean autoCommit = null;
		try {
			conn = DbKit.getConnection();
			autoCommit = conn.getAutoCommit();
			conn.setAutoCommit(false);
			return batch(conn, sqlList, batchSize);
		} catch (Exception e) {
			throw new ActiveRecordException(e);
		} finally {
			if (autoCommit != null)
				try {
					conn.setAutoCommit(autoCommit);
				} catch (Exception e) {
					e.printStackTrace();
				}
			DbKit.close(conn);
		}
	}

	public static int[] batch(DataSource dataSource, List<String> sqlList, int batchSize) {
		Connection conn = null;
		Boolean autoCommit = null;
		try {
			conn = dataSource.getConnection();
			autoCommit = conn.getAutoCommit();
			conn.setAutoCommit(false);
			return batch(conn, sqlList, batchSize);
		} catch (Exception e) {
			throw new ActiveRecordException(e);
		} finally {
			if (autoCommit != null)
				try {
					conn.setAutoCommit(autoCommit);
				} catch (Exception e) {
					e.printStackTrace();
				}
			DbKit.closeYe(conn);
		}
	}
}
