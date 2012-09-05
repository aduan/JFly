package net.jfly.plugin.ActiveRecord;

import java.sql.Connection;
import java.sql.SQLException;

import net.jfly.aop.Interceptor;
import net.jfly.core.ActionInvocation;

public class OneConnectionPerThread implements Interceptor {

	public void intercept(ActionInvocation invocation) {
		Connection conn = null;
		try {
			conn = DbKit.getConnection();
			DbKit.setThreadLocalConnection(conn);
			invocation.invoke();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				DbKit.removeThreadLocalConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			DbKit.close(conn);
		}
	}

	public void intercept() {

	}
}
