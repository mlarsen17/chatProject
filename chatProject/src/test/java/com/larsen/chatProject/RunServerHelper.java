package com.larsen.chatProject;

import java.net.Socket;
import server.ChatServer;

public class RunServerHelper implements Runnable {
	private ChatServer chatServer;
	private int port;
	
	
	RunServerHelper(int port) {
		this.port = port;
		chatServer = new ChatServer( port);
	}
	
	public void run() {
		chatServer.startServer();
	}
	
	@SuppressWarnings("resource")
	public void killServer() {
		chatServer.setServerToKill();
		try {
			new Socket("localhost",port );
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
