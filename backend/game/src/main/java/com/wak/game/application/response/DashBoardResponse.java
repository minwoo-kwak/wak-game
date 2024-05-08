package com.wak.game.application.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class DashBoardResponse {
    private final String roomName;
    private final int aliveCount;//생존 유저 수
    private final int totalCount; //게임에 참여한 전체 참가자 수
    private final boolean isAlive;

    @Builder
    public DashBoardResponse(String roomName, int aliveCount, int totalCount, boolean isAlive) {
        this.roomName = roomName;
        this.aliveCount = aliveCount;
        this.totalCount = totalCount;
        this.isAlive = isAlive;
    }
}
