package com.wak.game.global.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorInfo {

    TEST(HttpStatus.INTERNAL_SERVER_ERROR, "TEST ERROR"),
    API_ERROR_ROOM_NOT_EXIST(HttpStatus.NOT_FOUND, "Room is not exist"),
    ;

    ErrorInfo(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    private final HttpStatus httpStatus;
    private final String message;

}
