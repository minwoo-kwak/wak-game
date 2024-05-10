package com.wak.chat.global.handler;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;

import com.wak.chat.global.token.JWTUtils;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class StompPreHandler implements ChannelInterceptor {

	private final JWTUtils jwtUtils;

	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

		final String header = String.valueOf(accessor.getNativeHeader("Authorization"));

		if (header == null || header.equals("null") || jwtUtils.isExpired(header.substring(7))) {
			throw new MessageDeliveryException("Unauthorized");
		}

		return message;
	}
}
