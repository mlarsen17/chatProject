package com.larsen.chatProject;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
	
	private static String url = "jdbc:sqlite:chatDB.db";
	
	private DBConnection() {}
	
	public static Connection getConnecton() {
		try {
			Connection conn = DriverManager.getConnection(url);
			return conn;
		} catch (SQLException e) {
			System.out.println(e);
			return null;
		}
	}
	
	public static boolean deleteDB() {
		File file = new File("chatDB.db");
		return file.delete();
	}
	
	

}
