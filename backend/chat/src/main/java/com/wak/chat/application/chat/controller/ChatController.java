package com.wak.chat.application.chat.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import com.wak.chat.application.chat.dto.ChatRequest;
import com.wak.chat.application.chat.dto.ChatResponse;
import com.wak.chat.domain.chat.service.ChatService;
import com.wak.chat.external.user.client.UserFeignClient;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ChatController {

	private final SimpMessagingTemplate simpMessagingTemplate;
	private final ChatService chatService;
	private final UserFeignClient userFeignClient;

	@MessageMapping("/lobby-chat")
	public void chatInLobby(ChatRequest chatRequest, @Header("Authorization") String token) {
		ChatResponse chatResponse = chatService.sendMessage(chatRequest, token);

		simpMessagingTemplate.convertAndSend("/topic/lobby-chat", chatResponse);
	}

	@MessageMapping("/chats/{roomId}")
	public void chatInGame(@DestinationVariable Long roomId, ChatRequest chatRequest, @Header("Authorization") String token) {
		ChatResponse chatResponse = chatService.sendMessage(roomId, chatRequest, token);

		simpMessagingTemplate.convertAndSend("/topic/chats/" + roomId, chatResponse);
	}

}
