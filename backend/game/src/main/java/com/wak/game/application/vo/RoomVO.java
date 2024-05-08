package com.wak.game.application.vo;

public record RoomVO(Long user_id, String color, String nickname, String team, boolean isChief) {
    public RoomVO(Long user_id, String color, String nickname, String team, boolean isChief) {
        this.user_id = user_id;
        this.color = color;
        this.nickname = nickname;
        this.team = team;
        this.isChief = isChief;
    }
}