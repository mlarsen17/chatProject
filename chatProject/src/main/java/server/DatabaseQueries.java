package server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.larsen.chatProject.dto.ActiveChatDTO;
import com.larsen.chatProject.dto.ChatDTO;
import com.larsen.chatProject.dto.MessageDTO;
import com.larsen.chatProject.dto.UserDTO;

public class DatabaseQueries {
	
	private DatabaseQueries() {};
	
	public static UserDTO createUser(Connection conn, String username) {
        String sql = "INSERT INTO user(username) VALUES(?)";
        int userId = 0;
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.executeUpdate();
            userId = pstmt.getGeneratedKeys().getInt(1);
            return getUserById(conn, userId);
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
	
	public static UserDTO getUserById(Connection conn, int userId) {
		String sql = "SELECT user_id, username FROM user WHERE user_id = ?";
		UserDTO userDTO = null;
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1,userId);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				userDTO = new UserDTO(rs.getInt(1),rs.getString(2));
			}
		} catch (SQLException e) {
            e.printStackTrace();
        }
		return userDTO;
	}
	
	public static UserDTO getUserByUsername(Connection conn, String username) {
		String sql = "SELECT user_id, username FROM user WHERE username = ?";
		UserDTO userDTO = null;
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1,username);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				userDTO = new UserDTO(rs.getInt(1),rs.getString(2));
			}
		} catch (SQLException e) {
            e.printStackTrace();
        }
		return userDTO;
	}
	
	public static List<UserDTO> getUserList(Connection conn) {
		String sql = "SELECT user_id, username FROM user";
		List<UserDTO> userDTOList = new ArrayList<>();
		try (Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {
			while(rs.next()) {
				userDTOList.add(new UserDTO(rs.getInt(1),rs.getString(2)));
			}
		} catch (SQLException e) {
            e.printStackTrace();
        }
		return userDTOList;
	}
	
	public static ChatDTO createChat(Connection conn, String chatName) {
        System.out.println("Attempting to create new chat");
		String sql = "INSERT INTO chat(chat_name) VALUES(?)";
        int chatId = 0;
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, chatName);
            pstmt.executeUpdate();
            chatId = pstmt.getGeneratedKeys().getInt(1);
            return getChatById(conn, chatId);
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
        
    }
	
	public static ChatDTO getChatById(Connection conn, int chatId) {
		String sql = "SELECT chat_id, chat_name, create_time FROM chat WHERE chat_id = ?";
		ChatDTO chatDTO = null;
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1,chatId);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
			chatDTO = new ChatDTO(rs.getInt(1),rs.getString(2),rs.getLong(3));
			}
		} catch (SQLException e) {
            e.printStackTrace();
        }
		return chatDTO;
	}
	
	public static ChatDTO getChatByUsername(Connection conn, String chatName) {
		String sql = "SELECT chat_id, chat_name, create_time FROM chat WHERE chat_name = ?";
		ChatDTO chatDTO = null;
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1,chatName);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
			chatDTO = new ChatDTO(rs.getInt(1),rs.getString(2),rs.getLong(3));
			}
		} catch (SQLException e) {
            e.printStackTrace();
        }
		return chatDTO;
	}
	
	public static List<ChatDTO> getChatList(Connection conn) {
		String sql = "SELECT chat_id, chat_name, create_time FROM chat";
		List<ChatDTO> chatDTOList = new ArrayList<>();
		try (Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {
			while(rs.next()) {
				chatDTOList.add(new ChatDTO(rs.getInt(1),rs.getString(2),rs.getLong(3)));
			}
		} catch (SQLException e) {
            e.printStackTrace();
        }
		return chatDTOList;
	}
	
	public static ActiveChatDTO getActiveUsers(Connection conn, ChatDTO chatDTO) {
		ActiveChatDTO activeChatDTO = new ActiveChatDTO();
		activeChatDTO.setChatDTO(chatDTO);
		String sql = "SELECT u.user_id, u.username \n"
				+ "FROM user u \n"
				+ "JOIN active_chat ac ON u.user_id = ac.user_id \n"
				+ "WHERE ac.chat_id = ?";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setInt(1, chatDTO.getChatId());
				ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				activeChatDTO.addUserDTO(new UserDTO(rs.getInt(1),rs.getString(2)));
			}
		} catch (SQLException e) {
            e.printStackTrace();
        }
		return activeChatDTO;
	}
	
	public static void createActiveUsers(Connection conn, ActiveChatDTO activeChatDTO) {
		String sql = "INSERT INTO active_chat(user_id, chat_id) VALUES (?,?)";
		int chatId = activeChatDTO.getChatDTO().getChatId();
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			for(UserDTO userDTO : activeChatDTO.getUserDTOList()) {
				pstmt.setInt(1, userDTO.getUserId());
				pstmt.setInt(2, chatId);
				pstmt.addBatch();
			}
			pstmt.executeBatch();
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void removeActiveUsers(Connection conn, ActiveChatDTO activeChatDTO) {
		String sql = "DELETE FROM active_chat WHERE user_id = ? AND chat_id = ?";
		int chatId = activeChatDTO.getChatDTO().getChatId();
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			for(UserDTO userDTO : activeChatDTO.getUserDTOList()) {
				pstmt.setInt(1, userDTO.getUserId());
				pstmt.setInt(2, chatId);
				pstmt.addBatch();
			}
			pstmt.executeBatch();
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	public static List<MessageDTO> getLast10Messages(Connection conn) {
		String sql = "SELECT message_id, chat_id, content, create_time, sent_from_user_id FROM message ORDER BY create_time DESC LIMIT 10";
		List<MessageDTO> messageDTOList = new ArrayList<>();
		try (Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {
			while(rs.next()) {
				messageDTOList.add(new MessageDTO(rs.getInt(1),rs.getInt(2),rs.getString(3),rs.getLong(4),rs.getInt(5)));
			}
		} catch (SQLException e) {
            e.printStackTrace();
        }
		return messageDTOList;
	}
		
		
	
	public static MessageDTO getMessageById(Connection conn, int messageId) {
		String sql = "SELECT message_id, chat_id, content, create_time, sent_from_user_id FROM message WHERE message_id = ?";
		MessageDTO messageDTO = null;
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1,messageId);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				messageDTO = new MessageDTO(rs.getInt(1),rs.getInt(2),rs.getString(3),rs.getLong(4),rs.getInt(5));
			}
		} catch (SQLException e) {
            e.printStackTrace();
        }
		return messageDTO;
	}
	
	
	
	public static MessageDTO createMessage(Connection conn, MessageDTO messageDTO) {
		String sql = "INSERT INTO message(chat_id, content, sent_from_user_id) VALUES (?,?,?)";
		int messageId;
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setInt(1, messageDTO.getChatId());
				pstmt.setString(2, messageDTO.getContent());
				pstmt.setInt(3, messageDTO.getSentFromUserId());
				pstmt.executeUpdate();
	            messageId = pstmt.getGeneratedKeys().getInt(1);
	            return getMessageById(conn, messageId);
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return messageDTO;
	}


}
