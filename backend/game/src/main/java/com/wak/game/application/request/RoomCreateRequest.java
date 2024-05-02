package com.wak.game.application.request;

import com.wak.game.domain.room.RoomType;

public record RoomCreateRequest (String room_name, String room_password, int limit_players, RoomType mode){
    public RoomCreateRequest(String room_name, String room_password, int limit_players, RoomType mode) {
        this.room_name = room_name;
        this.room_password = room_password;
        this.limit_players = limit_players;
        this.mode = mode;
    }
}
