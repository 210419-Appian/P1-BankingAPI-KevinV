package com.revature.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionUtil {

	public static Connection getConnection() throws SQLException {
		//register our driver. other frameworks need to access this driver.
		try {
			Class.forName("org.postgresql.Driver");
		}catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		//give our Database Credentials
		String url = "jdbc:postgresql://appian-210419.cadgsfb9kbuu.us-east-2.rds.amazonaws.com:5432/postgres";
		String username = "postgres";	//ideally, use env variables to hide this information
		String password = "password";	//System.getenv("keyName");
		
		return DriverManager.getConnection(url, username, password);
	}
	
	public static void main(String[] args) {
		/**
		 * Finally blocks are used to close connections/resources/streams.
		 * "Try with close" declares a resource to open and then close at the try declaration.
		 */
		
		try(Connection conn=ConnectionUtil.getConnection()){
			System.out.println("Connection successful");
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
	}
}
