package com.wak.game.application.response.socket;

import com.wak.game.domain.room.dto.RoomInfo;

import java.util.List;

public record RoomListResponse (int totalPage, List<RoomInfo> rooms){
    public RoomListResponse(int totalPage, List<RoomInfo> rooms) {
        this.totalPage = totalPage;
        this.rooms = rooms;
    }
}
