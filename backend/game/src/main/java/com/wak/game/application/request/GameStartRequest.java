package com.wak.game.application.request;

import lombok.*;

@Getter
@ToString
@NoArgsConstructor
public class GameStartRequest {
    private Long roomId;
    private int roundNumber;
    private String comment;
    private boolean showNickname;

    @Builder
    public GameStartRequest(Long roomId, int roundNumber, String comment, boolean showNickname) {
        this.roomId = roomId;
        this.roundNumber = roundNumber;
        this.comment = comment;
        this.showNickname = showNickname;
    }
}
