package com.wak.game.global.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorInfo {

    TEST(HttpStatus.INTERNAL_SERVER_ERROR, "TEST ERROR"),

    /* COLOR */
    COLOR_NOT_EXIST(HttpStatus.INTERNAL_SERVER_ERROR, "COLOR IS NOT FOUND"),


    /* USER */
    USER_NOT_EXIST(HttpStatus.INTERNAL_SERVER_ERROR, "USER IS NOT FOUND"),
    USER_ALREADY_EXIST(HttpStatus.INTERNAL_SERVER_ERROR, "USER IS ALREADY FOUND"),


    /* CHAT */
    /* PLAYER */
    /* PLAYER LOG*/
    /* ROOM */
    /* ROOM LOG*/
    /* ROUND */
    ;

    ErrorInfo(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    private final HttpStatus httpStatus;
    private final String message;

}
