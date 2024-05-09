package com.wak.chat.domain.chat.service;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Service;

import com.wak.chat.application.chat.dto.ChatLog;
import com.wak.chat.application.chat.dto.ChatRequest;
import com.wak.chat.application.chat.dto.ChatResponse;
import com.wak.chat.external.user.client.UserFeignClient;
import com.wak.chat.global.token.JWTUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatService {

	private final UserFeignClient userFeignClient;
	private final MongoOperations operations;
	private final JWTUtils jwtUtils;

	public ChatResponse sendMessage(ChatRequest chatRequest, String token) {
		Long userId = jwtUtils.getId(token.substring(7));

		ChatLog chatLog = new ChatLog(userId, chatRequest.getMessage());
		operations.insert(chatLog, "lobby");

		return ChatResponse.builder()
			.sender(getUserInfo(token))
			.color(chatRequest.getColor())
			.message(chatRequest.getMessage())
			.build();
	}

	public ChatResponse sendMessage(Long roomId, ChatRequest chatRequest, String token) {
		Long userId = jwtUtils.getId(token.substring(7));

		ChatLog chatLog = new ChatLog(userId, chatRequest.getMessage());
		operations.insert(chatLog, "room-" + roomId);

		return ChatResponse.builder()
			.sender(getUserInfo(token))
			.color(chatRequest.getColor())
			.message(chatRequest.getMessage())
			.build();
	}

	public String getUserInfo(String token) {
		return userFeignClient.getUserInfo(token).getData().nickname();
	}

}
