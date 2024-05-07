package com.wak.game.application.controller.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class UserInRoomRequest {
    private long roomId;
    private long userid;
    private String hexColor;
    private String nickname;
    private int team;
    private boolean isChief;

    @Builder
    public UserInRoomRequest(long roomId, long userid, String hexColor, String nickname, int team, boolean isChief) {
        this.roomId = roomId;
        this.userid = userid;
        this.hexColor = hexColor;
        this.nickname = nickname;
        this.team = team;
        this.isChief = isChief;
    }

}
