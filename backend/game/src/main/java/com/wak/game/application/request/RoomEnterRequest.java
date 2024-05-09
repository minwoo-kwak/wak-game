package com.wak.game.application.request;

public record RoomEnterRequest (String roomPassword){
    public RoomEnterRequest(String roomPassword) {
        this.roomPassword = roomPassword;
    }
}
