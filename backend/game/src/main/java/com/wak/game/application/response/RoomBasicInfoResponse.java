package com.wak.game.application.response;

public record RoomBasicInfoResponse(long userId, boolean isHost, long roomId, String roomName, String mode, int limitPlayers, boolean isPublic){

    public static RoomBasicInfoResponse of (long user_id, boolean isChief, long room_id, String room_name, String mode, int limit_players, boolean isPublic) {
        return new RoomBasicInfoResponse(user_id, isChief, room_id, room_name, mode, limit_players, isPublic);
    }

    public RoomBasicInfoResponse(long userId, boolean isHost, long roomId, String roomName, String mode, int limitPlayers, boolean isPublic) {
        this.userId = userId;
        this.isHost = isHost;
        this.roomId = roomId;
        this.roomName = roomName;
        this.mode = mode;
        this.limitPlayers = limitPlayers;
        this.isPublic = isPublic;
    }
}
