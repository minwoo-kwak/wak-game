package com.wak.game.application.request;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class UserInRoomRequest {
    private long roomId;
    private long userId;
    private String color;
    private String nickname;
    private int team;
    private boolean isHost;

    @Builder
    public UserInRoomRequest(long roomId, long userId, String color, String nickname, int team, boolean isHost) {
        this.roomId = roomId;
        this.userId = userId;
        this.color = color;
        this.nickname = nickname;
        this.team = team;
        this.isHost = isHost;
    }

}
