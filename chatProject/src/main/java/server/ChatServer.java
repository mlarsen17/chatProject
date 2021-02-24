package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;

import com.larsen.chatProject.DBConnection;
import com.larsen.chatProject.dto.ActiveChatDTO;
import com.larsen.chatProject.dto.ChatDTO;
import com.larsen.chatProject.dto.MessageDTO;
import com.larsen.chatProject.dto.UserDTO;

public class ChatServer {

	private int port;
	private Connection conn;
	private HashMap<String, ChatUserThread> chatUserThreadMap = new HashMap<>();
	private boolean killServer = false;
	
	public ChatServer(int port) {
		this.port = port;
	}
	
	public void startServer() {
		conn = DBConnection.getConnecton();
		try (ServerSocket serverSocket = new ServerSocket(port)) {
			System.out.println("Chat Server is listening on port " + port);
			
			while(!killServer) {
				Socket socket = serverSocket.accept();
				
				System.out.println("User attempting to connect");
				ChatUserThread chatUserThread = new ChatUserThread(socket,this,killServer);
				chatUserThread.start();
			}
			System.out.println("Server Shutting Down");
			
			
		} catch (IOException e) {
			System.out.println("Error starting server");
			e.printStackTrace();
		}
	}
	
	public UserDTO addUser(String username) {
		UserDTO userDTO = DatabaseQueries.getUserByUsername(conn, username);
		UserDTO outputUserDTO = null;
		if(userDTO == null) {
			outputUserDTO = DatabaseQueries.createUser(conn, username);
			return outputUserDTO;
		} else {
			return new UserDTO();
		}
	}
	
	public void addUserToChat(ActiveChatDTO activeChatDTO) {
		DatabaseQueries.createActiveUsers(conn, activeChatDTO);
	}
	
	public ChatDTO addChat(String chatName) {
		ChatDTO chatDTO = DatabaseQueries.getChatByUsername(conn, chatName);
		//If chat already exists it shouldn't be recreated
		if(chatDTO != null) {
			return new ChatDTO();
		} else {
			return DatabaseQueries.createChat(conn, chatName);
		}
	}
	
	public void writeMessageToChat(MessageDTO messageDTO) {
		MessageDTO returnMessage = DatabaseQueries.createMessage(conn, messageDTO);
		ChatDTO chatDTO = new ChatDTO();
		chatDTO.setChatId(messageDTO.getChatId());
		ActiveChatDTO activeChatDTO = DatabaseQueries.getActiveUsers(conn, chatDTO);
		List<UserDTO> userDTOList = activeChatDTO.getUserDTOList();
		for( UserDTO userDTO : userDTOList) {
			ChatUserThread chatUserThread = chatUserThreadMap.get(userDTO.getUsername());
			chatUserThread.writeMessageToUser(returnMessage);
		}	
	}
	
	public void removeActiveUserFromChat(ActiveChatDTO activeChatDTO) {
		DatabaseQueries.removeActiveUsers(conn, activeChatDTO);
	}
	
	public void setServerToKill() {
		killServer = true;
	}
	
	public void addUserThread(String username, ChatUserThread chatUserThread) {
		chatUserThreadMap.put(username, chatUserThread);
	}
	
	
	
	
}
