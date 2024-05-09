package com.wak.game.application.response.socket;

import com.wak.game.application.vo.RoomInfoVO;

import java.util.List;

public record RoomListResponse (int totalPage, List<RoomInfoVO> rooms){
    public RoomListResponse(int totalPage, List<RoomInfoVO> rooms) {
        this.totalPage = totalPage;
        this.rooms = rooms;
    }
}
