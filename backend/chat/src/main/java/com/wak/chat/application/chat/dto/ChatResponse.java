package com.wak.chat.application.chat.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatResponse {

	private String sender;
	private String color;
	private String message;

}
