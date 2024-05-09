package com.wak.game.application.response;

public record RoomCreateResponse (Long roomId){

    public static RoomCreateResponse of (Long roomId) {
        return new RoomCreateResponse(roomId);
    }

    public RoomCreateResponse(Long roomId) {this.roomId = roomId;}
}
