package com.wak.game.application.response;


import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class PlayerInfoResponse{
    private final Long roundId;
    private final Long userId;
    private final int stamina;
    private final String hexColor;
    private final String team;
    private final String nickname;

    @Builder
    public PlayerInfoResponse(Long roundId, Long userId, int stamina, String team, String hexColor, String nickname) {
        this.roundId = roundId;
        this.userId = userId;
        this.stamina = stamina;
        this.team = team;
        this.hexColor = hexColor;
        this.nickname = nickname;
    }
}
