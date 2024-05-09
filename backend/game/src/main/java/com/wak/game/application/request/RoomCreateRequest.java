package com.wak.game.application.request;

import com.wak.game.domain.room.RoomType;

public record RoomCreateRequest (String roomName, String roomPassword, short limitPlayers, RoomType mode){
    public RoomCreateRequest(String roomName, String roomPassword, short limitPlayers, RoomType mode) {
        this.roomName = roomName;
        this.roomPassword = roomPassword;
        this.limitPlayers = limitPlayers;
        this.mode = mode;
    }
}
