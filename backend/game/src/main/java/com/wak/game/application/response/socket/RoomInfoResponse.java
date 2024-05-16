package com.wak.game.application.response.socket;

import com.wak.game.application.vo.RoomVO;

import java.util.List;

public record RoomInfoResponse (Long roomId, int currentPlayers, boolean isStart, List<RoomVO> users){
    public RoomInfoResponse(Long roomId, int currentPlayers,  boolean isStart, List<RoomVO> users) {
        this.roomId = roomId;
        this.currentPlayers = currentPlayers;
        this.isStart = isStart;
        this.users = users;
    }
}
