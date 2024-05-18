package com.wak.game.application.vo;

import lombok.Getter;

public record clickVO(Long userId, Long victimId, Long roundId, String time, Long nanoSec) {
}