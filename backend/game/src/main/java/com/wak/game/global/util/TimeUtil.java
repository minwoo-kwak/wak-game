package com.wak.game.global.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class TimeUtil {
    public String getCurrentTimeInNanos() {
        long nanos = System.nanoTime();
        return String.valueOf(nanos);
    }
}
