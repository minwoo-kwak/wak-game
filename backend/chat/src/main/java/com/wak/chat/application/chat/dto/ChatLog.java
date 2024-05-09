package com.wak.chat.application.chat.dto;

import java.util.Date;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ChatLog {

	private Long userId;
	private String message;
	@CreatedDate
	@DateTimeFormat(pattern = "yyyy-mm-dd hh:mm:ss")
	private Date createdAt;

	public ChatLog(Long userId, String message) {
		this.userId = userId;
		this.message = message;
	}

}
