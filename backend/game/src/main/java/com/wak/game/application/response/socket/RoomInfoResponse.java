package com.wak.game.application.response.socket;

import com.wak.game.application.vo.RoomVO;

import java.util.List;

public record RoomInfoResponse (Long room_id, int current_players, Long user_id, List<RoomVO> users){
    public RoomInfoResponse(Long room_id, int current_players, Long user_id, List<RoomVO> users) {
        this.room_id = room_id;
        this.current_players = current_players;
        this.user_id = user_id;
        this.users = users;
    }
}
