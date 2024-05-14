package com.wak.game.application.response.socket;

import com.wak.game.application.vo.RoomVO;

import java.util.List;

public record KillLogResponse(Long roundId, String userNickname, String color, String victimNickName, String victimColor){
    public KillLogResponse(Long roundId, String userNickname, String color, String victimNickName, String victimColor) {
        this.roundId = roundId;
        this.userNickname = userNickname;
        this.color = color;
        this.victimNickName = victimNickName;
        this.victimColor = victimColor;
    }
}
