package com.wak.game.domain.rank.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class RankInfo {
    private Long userId;
    private String nickname;
    private int killCnt;
    private String color;

    @Builder
    public RankInfo(Long userId, String nickname, int killCnt, String color) {
        this.userId = userId;
        this.nickname = nickname;
        this.killCnt = killCnt;
        this.color = color;
    }
    public void updateKill() {
        ++this.killCnt;
    }
}
