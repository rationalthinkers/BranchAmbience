package com.branch.jdbc;

import java.sql.Connection;
import java.util.Properties;

import com.branch.util.services.VCAP_SERVICES;

public class JDBCConnection {

	// private static String ServerName = VCAP_SERVICES.get("sqldb", "0",
	// "credentials", "hostname");
	// private static int PortNumber
	// =Integer.parseInt(VCAP_SERVICES.get("sqldb", "0", "credentials",
	// "port"));
	// private static String DatabaseName = VCAP_SERVICES.get("sqldb", "0",
	// "credentials", "db");
	private static String jdbcurl = VCAP_SERVICES.get("sqldb", "0", "credentials", "jdbcurl");
	private static String user = VCAP_SERVICES.get("sqldb", "0", "credentials", "username");
	private static String userPassword = VCAP_SERVICES.get("sqldb", "0", "credentials", "password");

	private static Properties properties = new Properties();

	public static Connection getConnection() {

		// String url = "jdbc:db2://" + ServerName + ":" + PortNumber + "/" +
		// DatabaseName;

		properties.put("user", user);
		properties.put("password", userPassword);
		properties.put("sslConnection", "false");

		java.sql.Connection con = null;
		try {
			Class.forName("com.ibm.db2.jcc.DB2Driver").newInstance();
		} catch (Exception e) {
			System.out.println("Error: failed to load Db2 jcc driver.");
		}
		try {
			con = java.sql.DriverManager.getConnection(jdbcurl, properties);
			if (con != null) {
				System.out.println("Success");
			} else {
				System.out.println("Failed to make the connection");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return con;
	}

	public static void closeConnection(Connection con) {

		if (con != null) {
			try {
				con.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}

	}
}
