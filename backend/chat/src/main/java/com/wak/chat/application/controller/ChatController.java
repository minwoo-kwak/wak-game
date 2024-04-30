package com.wak.chat.application.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import com.wak.chat.domain.chat.ChatRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ChatController {

	private final SimpMessagingTemplate simpMessagingTemplate;

	@MessageMapping("/lobby-chat")
	public void chatInLobby(ChatRequest chatRequest) {
		simpMessagingTemplate.convertAndSend("/topic/lobby-chat", chatRequest);
	}

	@MessageMapping("/chats/{roomId}")
	public void chatInGame(@DestinationVariable Long roomId, ChatRequest chatRequest) {
		simpMessagingTemplate.convertAndSend("/topic/chats/" + roomId, chatRequest);
	}

}
