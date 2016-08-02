package com.test;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcUtil {

	private JdbcUtil() {
	}
	
	/**
	 * 获得数据库的链接
	 * @param driver
	 * @param url
	 * @param username
	 * @param password
	 * @return
	 */
	public static Connection getConn(String driver, String url, String username, String password) {
		Connection connection = null;
		try {
			Class.forName(driver);
//			System.out.println("注册驱动成功!!");
			connection = DriverManager.getConnection(url, username, password);
			System.out.println("数据库连接成功");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return connection;
	}

	/**
	 * 释放连接，如果参数没有就设置为null
	 * @param connection
	 * @param pstmt
	 * @param resultSet
	 */
	public static void releaseConn(Connection connection, PreparedStatement pstmt, ResultSet resultSet) {
		if (resultSet != null) {
			try {
				resultSet.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (pstmt != null) {
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		String driver = "com.mysql.jdbc.Driver";
//		String url = "jdbc:mysql://localhost:3306/tms?useUnicode=true&characterEncoding=utf-8";
		String url = "jdbc:mysql://172.22.120.235:3306/tms?useUnicode=true&characterEncoding=utf-8";
		String username = "root";
		String password = "root";
		JdbcUtil.getConn(driver, url, username, password);
	}

}