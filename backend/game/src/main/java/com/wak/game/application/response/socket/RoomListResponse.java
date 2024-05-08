package com.wak.game.application.response.socket;

import com.wak.game.application.vo.RoomInfoVO;

import java.util.List;

public record RoomListResponse (int total_page, List<RoomInfoVO> rooms){
    public RoomListResponse(int total_page, List<RoomInfoVO> rooms) {
        this.total_page = total_page;
        this.rooms = rooms;
    }
}
