package com.larsen.chatProject.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ActiveChatDTO implements Serializable {
	ChatDTO chatDTO;
	List<UserDTO> userDTOList = new ArrayList<>();
	
	public ActiveChatDTO() {}
	
	public ChatDTO getChatDTO() {
		return chatDTO;
	}
	
	public void setChatDTO(ChatDTO chatDTO) {
		this.chatDTO = chatDTO;
	}

	public List<UserDTO> getUserDTOList() {
		return userDTOList;
	}

	public void setUserDTOList(List<UserDTO> userDTOList) {
		this.userDTOList = userDTOList;
	}
	
	public void addUserDTO(UserDTO userDTO) {
		this.userDTOList.add(userDTO);
	}
	
	public void addUserDTOList(List<UserDTO> userDTOList) {
		this.userDTOList.addAll(userDTOList);
	}

	@Override
	public String toString() {
		return "ActiveChatDTO [chatDTO=" + chatDTO + ", userDTOList=" + userDTOList + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((chatDTO == null) ? 0 : chatDTO.hashCode());
		result = prime * result + ((userDTOList == null) ? 0 : userDTOList.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ActiveChatDTO other = (ActiveChatDTO) obj;
		if (chatDTO == null) {
			if (other.chatDTO != null)
				return false;
		} else if (!chatDTO.equals(other.chatDTO))
			return false;
		if (userDTOList == null) {
			if (other.userDTOList != null)
				return false;
		} else if (!userDTOList.equals(other.userDTOList))
			return false;
		return true;
	}
	
	
	
	


	
	
}
