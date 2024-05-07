package com.wak.game.application.vo;

public record RoomVO(Long userId, String hexColor, String nickname, String team, boolean isChief) {
    public RoomVO(Long userId, String hexColor, String nickname, String team, boolean isChief) {
        this.userId = userId;
        this.hexColor = hexColor;
        this.nickname = nickname;
        this.team = team;
        this.isChief = isChief;
    }
}