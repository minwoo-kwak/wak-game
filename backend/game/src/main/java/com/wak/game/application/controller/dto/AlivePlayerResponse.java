package com.wak.game.application.controller.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AlivePlayerResponse {
    private Long roundId;
    private Long userId;
    private int stamina;
    private String team;
    private String hexColor;

    @Builder
    public AlivePlayerResponse(Long roundId, Long userId, int stamina, String team, String hexColor) {
        this.roundId = roundId;
        this.userId = userId;
        this.stamina = stamina;
        this.team = team;
        this.hexColor = hexColor;
    }
}
