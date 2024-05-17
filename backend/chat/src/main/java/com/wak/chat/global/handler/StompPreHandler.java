package com.wak.chat.global.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;

import com.wak.chat.global.token.JWTUtils;

import lombok.RequiredArgsConstructor;

import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class StompPreHandler implements ChannelInterceptor {

	private final JWTUtils jwtUtils;

	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

		StompCommand command = accessor.getCommand();
		if (command != StompCommand.SEND) {
			log.info("[{}] skip", command);
			return message;
		}

		List<String> authHeaders = accessor.getNativeHeader("Authorization");
		final String header = (authHeaders != null && !authHeaders.isEmpty()) ? authHeaders.get(0) : null;
		log.info("[AUTH] header={}", header);

		if (header == null || !header.startsWith("Bearer ") || jwtUtils.isExpired(header.substring(7))) {
			throw new MessageDeliveryException("Unauthorized");
		}

		return message;
	}
}
