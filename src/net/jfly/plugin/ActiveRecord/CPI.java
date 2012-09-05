package net.jfly.plugin.ActiveRecord;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Cross Package Invoking pattern for package activerecord.
 */
public abstract class CPI {
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static final Map<String, Object> getAttributeMap(Model model) {
		return model.getAttributeMap();
	}

	public static <T> List<T> query(Connection conn, String sql, Object... params) throws SQLException {
		return Db.query(conn, sql, params);
	}
}
