package com.larsen.chatProject;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class BuildDatabase {
	Connection conn;
	
	BuildDatabase(Connection conn) {
		this.conn = conn;
	}
	
	public void buildTables() {
		buildUserTable();
		buildChatTable();
		buildMessageTable();
		buildActiveChatTable();
		buildLastChatReadTable();
	}
	
	private void buildUserTable() {
		buildTable(
				"CREATE TABLE IF NOT EXISTS user (\n"
		                + "	user_id integer PRIMARY KEY,\n"
		                + "	username text NOT NULL\n"
		                + ");"
				);
		System.out.println("User table created");
	}
	
	private void buildChatTable() {
		buildTable(
				"CREATE TABLE IF NOT EXISTS chat (\n"
		                + "	chat_id integer PRIMARY KEY,\n"
		                + "	chat_name text NOT NULL,\n"
		                + "	create_time integer DEFAULT (cast(strftime('%s','now') as int))\n"
		                + ");"
				);
		System.out.println("Chat table created");
	}
	
	private void buildMessageTable() {
		buildTable(
				"CREATE TABLE IF NOT EXISTS message (\n"
						+ "	message_id integer PRIMARY KEY,\n"
						+ "	chat_id integer,\n"
		                + "	content text NOT NULL,\n"
		                + "	create_time integer DEFAULT (cast(strftime('%s','now') as int)),\n"
		                + "	sent_from_user_id integer \n"
		                + ");"
				);
		System.out.println("Message table created");
	}
	
	
	private void buildActiveChatTable() {
		buildTable(
				"CREATE TABLE IF NOT EXISTS active_chat (\n"
						+ "	user_id integer ,\n"
						+ "	chat_id integer \n"
		                + ");"
				);
		System.out.println("Active Chat table created");
	}
	
	private void buildLastChatReadTable() {
		buildTable(
				"CREATE TABLE IF NOT EXISTS chat_last_read (\n"
						+ "	user_id integer ,\n"
						+ "	chat_id integer ,\n"
						+ "	last_read_time integer \n"
		                + ");"
				);
		System.out.println("Last Chat Read table created");
	}
	
	private void buildTable(String ddl) {
	    try (Statement stmt = conn.createStatement()) {
	            // create a new table
	        stmt.execute(ddl);
	    } catch (SQLException e) {
	        System.out.println(e.getMessage());
	        e.printStackTrace();

	    }
	        
	}
}
