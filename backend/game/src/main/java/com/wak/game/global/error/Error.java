package com.wak.game.global.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum Error {

    TEST(HttpStatus.INTERNAL_SERVER_ERROR, "TEST ERROR")
    ;

    Error(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    private final HttpStatus httpStatus;
    private final String message;

}
