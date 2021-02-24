package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.Connection;
import java.util.List;

import com.larsen.chatProject.DBConnection;
import com.larsen.chatProject.dto.ActiveChatDTO;
import com.larsen.chatProject.dto.ChatDTO;
import com.larsen.chatProject.dto.MessageDTO;
import com.larsen.chatProject.dto.UserDTO;

public class ChatUserThread extends Thread{
	private Socket socket;
	private ChatServer chatServer;
	private ObjectOutputStream writer;
	private ObjectInputStream reader;
	private UserDTO userDTO;
	private boolean disconnect = false;
	private ChatDTO chatDTO;
	private Connection conn = DBConnection.getConnecton();
	
	public ChatUserThread(Socket socket, ChatServer chatServer, boolean disconnect) {
		this.socket = socket;
		this.chatServer = chatServer;
	}
	
	public void run() {
		try {
			if(disconnect) {
				return;
			}
			System.out.println("Attempting to build reader/writer");
			writer = new ObjectOutputStream(socket.getOutputStream());
			System.out.println("Built writer");
			reader = new ObjectInputStream(socket.getInputStream());
			System.out.println("Built reader");
			System.out.println("User connected");
			while(!disconnect) {
				Object inputDTO = reader.readObject();
				System.out.println("Recieved " + inputDTO);
				if(inputDTO instanceof UserDTO) {
					//When client connects it should submit a UserDTO with desired username
					UserDTO userDTO = (UserDTO) inputDTO;
					System.out.println("Recieved " + userDTO);
					//If the user doesn't exist, they are added as this user/
					UserDTO outputDTO = chatServer.addUser(userDTO.getUsername());
					if(outputDTO.getUserId() > 0) {
						this.userDTO = outputDTO;
						chatServer.addUserThread(userDTO.getUsername(), this);
					}
					writer.writeObject(outputDTO);
				} else if(inputDTO instanceof ChatDTO) {
					ChatDTO chatDTO = (ChatDTO) inputDTO;
					//If chatDTO is empty respond to client with list of chats.
					if(chatDTO.equals(new ChatDTO())) {
						List<ChatDTO> chatDTOList = DatabaseQueries.getChatList(conn);
						writer.writeObject(chatDTOList);
					//If chatDTO has both an ID and a chatname, join the chat.	
					} else if(chatDTO.getChatId() != null && chatDTO.getChatName() != null) {
						ActiveChatDTO activeChatDTO = new ActiveChatDTO();
						activeChatDTO.addUserDTO(userDTO);
						activeChatDTO.setChatDTO(chatDTO);
						chatServer.addUserToChat(activeChatDTO);
						this.chatDTO = chatDTO;
						List<MessageDTO> messageDTOList = DatabaseQueries.getLast10Messages(conn);
						writer.writeObject(messageDTOList);
					//If the chat has a name but no ID, attempt to create the chat.	
					} else if(chatDTO.getChatId() == null && chatDTO.getChatName() != null){
						System.out.println("Attempting to create chat" + chatDTO);
						ChatDTO outputChatDTO = chatServer.addChat(chatDTO.getChatName());
						writer.writeObject(outputChatDTO);
					}
				} else if(inputDTO instanceof MessageDTO) {
					MessageDTO messageDTO = (MessageDTO) inputDTO;
					//If the chat message is EXIT, just leave the chat
					if(messageDTO.getContent().equals("EXIT")) {
						ActiveChatDTO activeChatDTO = new ActiveChatDTO();
						activeChatDTO.setChatDTO(chatDTO);
						activeChatDTO.addUserDTO(userDTO);
						chatServer.removeActiveUserFromChat(activeChatDTO);
						this.chatDTO = null;
					//Otherwise write the message to the chat
					} else {
						chatServer.writeMessageToChat(messageDTO);
					}
					
				}
			}
		} catch (IOException | ClassNotFoundException e) {
			System.out.println("Error in the chatUserThread: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void writeMessageToUser(MessageDTO messageDTO) {
		try {
			writer.writeObject(messageDTO);
		} catch (IOException e) {
			System.out.println("There was an issue writing the message DTO ");
			e.printStackTrace();
		}
	}
}
