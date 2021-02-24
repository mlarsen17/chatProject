package com.larsen.chatProject.dto;

import java.io.Serializable;

public class ChatDTO implements Serializable {
	private Integer chatId;
	private String chatName;
	private Long createTime;
	
	public ChatDTO() {}

	public ChatDTO(Integer chatId, String chatName, Long createTime) {
		this.chatId = chatId;
		this.chatName = chatName;
		this.createTime = createTime;
	}
	
	ChatDTO(ChatDTO chatDTO) {
		this.chatId = chatDTO.chatId;
		this.chatName = chatDTO.chatName;
		this.createTime = chatDTO.createTime;
	}

	public Integer getChatId() {
		return chatId;
	}

	public void setChatId(Integer chatId) {
		this.chatId = chatId;
	}

	public String getChatName() {
		return chatName;
	}

	public void setChatName(String chatName) {
		this.chatName = chatName;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	@Override
	public String toString() {
		return "ChatDTO [chatId=" + chatId + ", chatName=" + chatName + ", createTime=" + createTime + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((chatId == null) ? 0 : chatId.hashCode());
		result = prime * result + ((chatName == null) ? 0 : chatName.hashCode());
		result = prime * result + ((createTime == null) ? 0 : createTime.hashCode());
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
		ChatDTO other = (ChatDTO) obj;
		if (chatId == null) {
			if (other.chatId != null)
				return false;
		} else if (!chatId.equals(other.chatId))
			return false;
		if (chatName == null) {
			if (other.chatName != null)
				return false;
		} else if (!chatName.equals(other.chatName))
			return false;
		if (createTime == null) {
			if (other.createTime != null)
				return false;
		} else if (!createTime.equals(other.createTime))
			return false;
		return true;
	};
	
	
	
	
	
	
	
	
}
