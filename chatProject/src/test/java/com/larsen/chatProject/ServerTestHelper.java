package com.larsen.chatProject;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerTestHelper {
	private int port;
	private String host;
	private Socket socket;
	private ObjectOutputStream writer;
	private ObjectInputStream reader;
	
	public ServerTestHelper(String host, int port) {
		this.host = host;
		this.port = port;
		
	}
	
	public void createConnection() {
		try {
			socket = new Socket(host, port );
			System.out.println("Connected to chat server");
			reader = new ObjectInputStream(socket.getInputStream());
			writer = new ObjectOutputStream(socket.getOutputStream());
		} catch (Exception e ) {
			e.printStackTrace();
		}
	}
	
	public Object read() throws Exception  {
		Object inputDTO = reader.readObject();
		return inputDTO;
	}
	
	public void write(Object outputDTO) throws Exception {
		writer.writeObject(outputDTO);
	}
	
	

}
