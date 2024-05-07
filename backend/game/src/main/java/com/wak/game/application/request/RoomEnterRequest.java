package com.wak.game.application.request;

public record RoomEnterRequest (String room_password){
    public RoomEnterRequest(String room_password) {
        this.room_password = room_password;
    }
}
