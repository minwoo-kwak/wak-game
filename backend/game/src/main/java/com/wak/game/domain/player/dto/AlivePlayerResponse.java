package com.wak.game.domain.player.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AlivePlayerResponse {
    private Integer stamina;
    private String nickname;
    private String color;

    @Builder
    public AlivePlayerResponse(Integer stamina, String nickname, String color) {
        this.stamina = stamina;
        this.nickname = nickname;
        this.color = color;
    }
}
