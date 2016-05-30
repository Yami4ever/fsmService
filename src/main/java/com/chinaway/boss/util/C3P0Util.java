package com.chinaway.boss.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * C3P0 Util
 * 
 * @author zhuge
 *
 */
public class C3P0Util {

	private static Logger log = LoggerFactory.getLogger(C3P0Util.class);

	static ComboPooledDataSource cpds = null;

	static {
		cpds = new ComboPooledDataSource("mysql"); // mysql数据库
	}

	/**
	 * 获得数据库连接
	 * 
	 * @return Connection
	 */
	public static Connection getConnection() {
		try {
			return cpds.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 数据库关闭操作
	 * 
	 * @param conn
	 * @param st
	 * @param pst
	 * @param rs
	 */
	public static void close(Connection conn, Statement pst, ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (pst != null) {
			try {
				pst.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 在一个数据库连接上执行一个静态SQL语句查询
	 * 
	 * @param conn
	 *            数据库连接
	 * @param staticSql
	 *            静态SQL语句字符串
	 * @return 返回查询结果集ResultSet对象
	 */
	public static ResultSet executeQuery(Connection conn, String staticSql) {
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(staticSql);
		} catch (SQLException e) {
			log.error("#ERROR# :执行SQL语句出错，请检查！\n" + staticSql, e);
		} finally {
			close(conn, stmt, null);
		}
		return rs;
	}

	/**
	 * 在一个数据库连接上执行一个静态SQL语句
	 * 
	 * @param conn
	 *            数据库连接
	 * @param staticSql
	 *            静态SQL语句字符串
	 */
	public static void executeSQL(Connection conn, String staticSql) {
		Statement stmt = null;
		
		try {
			stmt = conn.createStatement();
			stmt.execute(staticSql);
		} catch (SQLException e) {
			log.error("#ERROR# :执行SQL语句出错，请检查！\n" + staticSql, e);
		} finally {
			close(conn, stmt, null);
		}
	}
	
	public static void executeSQL(Connection conn, String staticSql, Object... args) {
		PreparedStatement preparedStatement = null;

		try {
			preparedStatement = conn.prepareStatement(staticSql);
			for(int i = 0; i < args.length; i++){
				preparedStatement.setObject(i + 1, args[i]);
	        }
			preparedStatement.execute();
		} catch (SQLException e) {
			log.error("#ERROR# :执行SQL语句出错，请检查！\n" + staticSql, e);
		} finally {
			close(conn, preparedStatement, null);
		}
	}
	

	/**
	 * 在一个数据库连接上执行一批静态SQL语句
	 * 
	 * @param conn
	 *            数据库连接
	 * @param sqlList
	 *            静态SQL语句字符串集合
	 */
	public static void executeBatchSQL(Connection conn, List<String> sqlList) {
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			for (String sql : sqlList) {
				stmt.addBatch(sql);
			}
			stmt.executeBatch();
		} catch (SQLException e) {
			log.error("#ERROR# :执行批量SQL语句出错，请检查！", e);
		} finally {
			close(conn, stmt, null);
		}
	}

}
