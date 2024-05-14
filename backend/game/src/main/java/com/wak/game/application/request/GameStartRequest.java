package com.wak.game.application.request;

import lombok.*;

@Getter
@ToString
@NoArgsConstructor
public class GameStartRequest {
    private String comment;
    private boolean showNickname;

    @Builder
    public GameStartRequest(String comment, boolean showNickname) {
        this.comment = comment;
        this.showNickname = showNickname;
    }
}
