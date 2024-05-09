package com.wak.chat.external.user.dto;

public record UserInfoResponse (String nickname){
	public static UserInfoResponse of (String nickname) {
		return new UserInfoResponse(nickname);
	}

	public UserInfoResponse(String nickname){
		this.nickname = nickname;
	}
}
