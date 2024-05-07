package com.wak.game.application.response.socket;

import com.wak.game.application.vo.RoomVO;

import java.util.List;

public record RoomInfoResponse (Long room_id, String room_name, int current_players, int limit_players, String mode, Long user_id, List<RoomVO> users){
    public RoomInfoResponse(Long room_id, String room_name, int current_players, int limit_players, String mode, Long user_id, List<RoomVO> users) {
        this.room_id = room_id;
        this.room_name = room_name;
        this.current_players = current_players;
        this.limit_players = limit_players;
        this.mode = mode;
        this.user_id = user_id;
        this.users = users;
    }
}
