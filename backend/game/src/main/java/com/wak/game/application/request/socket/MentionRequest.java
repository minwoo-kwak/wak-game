package com.wak.game.application.request.socket;

public record MentionRequest(Long userId, Long roomId, Long roundId, String mention) {
    public MentionRequest {
    }
}
