package com.wak.game.application.response;

public record RoomBasicInfoResponse(long user_id, boolean isChief, long room_id, String room_name, String mode, int limit_players){

    public static RoomBasicInfoResponse of (long user_id, boolean isChief, long room_id, String room_name, String mode, int limit_players) {
        return new RoomBasicInfoResponse(user_id, isChief, room_id, room_name, mode, limit_players);
    }
    public RoomBasicInfoResponse(long user_id, boolean isChief, long room_id, String room_name, String mode, int limit_players) {
        this.user_id = user_id;
        this.isChief = isChief;
        this.room_id = room_id;
        this.room_name = room_name;
        this.mode = mode;
        this.limit_players = limit_players;
    }
}
