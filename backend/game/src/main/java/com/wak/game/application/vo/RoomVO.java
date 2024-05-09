package com.wak.game.application.vo;

public record RoomVO(Long userId, String color, String nickname, String team, boolean isHost) {
    public RoomVO(Long userId, String color, String nickname, String team, boolean isHost) {
        this.userId = userId;
        this.color = color;
        this.nickname = nickname;
        this.team = team;
        this.isHost = isHost;
    }
}