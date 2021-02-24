package com.larsen.chatProject.dto;

import java.io.Serializable;

public class MessageDTO implements Serializable {
	private Integer messageId;
	private Integer chatId;
	private String content;
	private Long createTime;
	private Integer sentFromUserId;
	
	public MessageDTO() {}

	public MessageDTO(Integer messageId, Integer chatId, String content, Long createTime, Integer sentFromUserId) {
		this.messageId = messageId;
		this.chatId = chatId;
		this.content = content;
		this.createTime = createTime;
		this.sentFromUserId = sentFromUserId;
	}

	public Integer getMessageId() {
		return messageId;
	}

	public void setMessageId(Integer messageId) {
		this.messageId = messageId;
	}

	public Integer getChatId() {
		return chatId;
	}

	public void setChatId(Integer chatId) {
		this.chatId = chatId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public Integer getSentFromUserId() {
		return sentFromUserId;
	}

	public void setSentFromUserId(Integer sentFromUserId) {
		this.sentFromUserId = sentFromUserId;
	}

	@Override
	public String toString() {
		return "MessageDTO [messageId=" + messageId + ", chatId=" + chatId + ", content=" + content + ", createTime="
				+ createTime + ", sentFromUserId=" + sentFromUserId + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((chatId == null) ? 0 : chatId.hashCode());
		result = prime * result + ((content == null) ? 0 : content.hashCode());
		result = prime * result + ((createTime == null) ? 0 : createTime.hashCode());
		result = prime * result + ((messageId == null) ? 0 : messageId.hashCode());
		result = prime * result + ((sentFromUserId == null) ? 0 : sentFromUserId.hashCode());
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
		MessageDTO other = (MessageDTO) obj;
		if (chatId == null) {
			if (other.chatId != null)
				return false;
		} else if (!chatId.equals(other.chatId))
			return false;
		if (content == null) {
			if (other.content != null)
				return false;
		} else if (!content.equals(other.content))
			return false;
		if (createTime == null) {
			if (other.createTime != null)
				return false;
		} else if (!createTime.equals(other.createTime))
			return false;
		if (messageId == null) {
			if (other.messageId != null)
				return false;
		} else if (!messageId.equals(other.messageId))
			return false;
		if (sentFromUserId == null) {
			if (other.sentFromUserId != null)
				return false;
		} else if (!sentFromUserId.equals(other.sentFromUserId))
			return false;
		return true;
	};
	
	
	
	
	
	
}
