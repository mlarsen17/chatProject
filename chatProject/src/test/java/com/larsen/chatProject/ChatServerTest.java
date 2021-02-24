package com.larsen.chatProject;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.Socket;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.larsen.chatProject.dto.ActiveChatDTO;
import com.larsen.chatProject.dto.ChatDTO;
import com.larsen.chatProject.dto.MessageDTO;
import com.larsen.chatProject.dto.UserDTO;

import server.ChatServer;
import server.DatabaseQueries;


public class ChatServerTest 
{

	
	public RunServerHelper startServer() {
		RunServerHelper runServerHelper = new RunServerHelper(4445);
		Thread thread = new Thread(runServerHelper);
		thread.start();
		return runServerHelper;
	}
    
    @Before
    public void buildDB() throws SQLException {
    	System.out.println(DBConnection.deleteDB() ? "DB deleted" : "Failed to Delete DB");
    	Connection conn = DBConnection.getConnecton();
    	(new BuildDatabase(conn)).buildTables();
    	conn.close();
    }
	
	@Test
    public void AddChat() throws Exception {
		RunServerHelper runServerHelper = startServer();
		ServerTestHelper helper = new ServerTestHelper("localhost", 4445);
		helper.createConnection();
		
    	ChatDTO chatDTO = new ChatDTO();
    	chatDTO.setChatName("test");
    	System.out.println("Writing chatDTO to server");
    	helper.write(chatDTO);
    	System.out.println("Reading chatDTO from server");
    	Object inputDTO = helper.read();
    	runServerHelper.killServer();
    	if(inputDTO instanceof ChatDTO) {
    		ChatDTO chatDTO1 = (ChatDTO) inputDTO;
    		assertNotNull(chatDTO1.getChatId());
    		
    		}
    	
    }
	
	@Test
    public void Adduser() throws Exception {
		RunServerHelper runServerHelper = startServer();
		ServerTestHelper helper = new ServerTestHelper("localhost", 4445);
		helper.createConnection();
		
    	UserDTO user = new UserDTO();
    	user.setUsername("marshall");
    	helper.write(user);
    	System.out.println("Reading chatDTO from server");
    	Object inputDTO = helper.read();

    	if(inputDTO instanceof UserDTO) {
    		UserDTO userDTO1 = (UserDTO) inputDTO;
    		assertNotNull(userDTO1.getUserId());
    		}
    	runServerHelper.killServer();
    	
    }
	
	@Test
    public void Addmessage() throws Exception {
		RunServerHelper runServerHelper = startServer();
		ServerTestHelper helper = new ServerTestHelper("localhost", 4445);
		helper.createConnection();
		
		Object returnDTO;
		UserDTO user = new UserDTO();
		user.setUsername("marshall-test");
		helper.write(user);
		returnDTO = helper.read();
		UserDTO returnUser = (UserDTO) returnDTO;
		
		ChatDTO chat = new ChatDTO();
		chat.setChatName("marshall-chat");
		System.out.println(chat);
		helper.write(chat);
		returnDTO = helper.read();
		ChatDTO returnChat = (ChatDTO) returnDTO;
		System.out.println(returnChat);
		helper.write(returnChat);
		returnDTO = helper.read();
		
		
		MessageDTO message = new MessageDTO();
    	message.setChatId(returnChat.getChatId());
		message.setSentFromUserId(returnUser.getUserId());
		message.setContent("testContent");
		System.out.println(message);
		helper.write(message);
    	Object inputDTO = helper.read();

    	if(inputDTO instanceof MessageDTO) {
    		MessageDTO returnMessage = (MessageDTO) inputDTO;
    		System.out.println(returnMessage);
    		assertNotNull(returnMessage.getMessageId());
    		}
    	runServerHelper.killServer();
    	
    }

}
