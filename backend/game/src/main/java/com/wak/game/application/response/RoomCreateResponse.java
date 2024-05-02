package com.wak.game.application.response;

public record RoomCreateResponse (Long room_id){

    public static RoomCreateResponse of (Long room_id) {
        return new RoomCreateResponse(room_id);
    }

    public RoomCreateResponse(Long room_id) {this.room_id = room_id;}
}
