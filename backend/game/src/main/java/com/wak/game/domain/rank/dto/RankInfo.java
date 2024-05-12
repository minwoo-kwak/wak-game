package com.wak.game.domain.rank.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class RankInfo {
    private Long userId;
    private String nickName;
    private int killCnt;

    @Builder
    public RankInfo(Long userId, String nickName, int killCnt) {
        this.userId = userId;
        this.nickName = nickName;
        this.killCnt = killCnt;
    }
}
