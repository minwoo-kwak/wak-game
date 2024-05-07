package com.wak.game.global.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorInfo {

    TEST(HttpStatus.INTERNAL_SERVER_ERROR, "TEST ERROR"),

    /* Sample */
    SAMPLE_NOT_EXIST(HttpStatus.BAD_REQUEST, "SAMPLE IS NOT FOUND") ,
    SAMPLE_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "SAMPLE IS ALREADY FOUND"),

    /* COLOR */
    COLOR_NOT_EXIST(HttpStatus.INTERNAL_SERVER_ERROR, "COLOR IS NOT FOUND"),


    /* USER */
    USER_NOT_EXIST(HttpStatus.NOT_FOUND, "USER IS NOT FOUND"),
    USER_ALREADY_EXIST(HttpStatus.NOT_FOUND, "USER IS ALREADY FOUND"),

    /* AUTH */
    AUTH_CLIENT_BY_AUTHORIZATION_INFORMATION(HttpStatus.NOT_FOUND, "NO AUTH INFORMATION"),
    AUTH_CLIENT_BY_JWT_SIGNATURE_INVALID(HttpStatus.NOT_FOUND, "SIGNATURE IS WRONG"),
    AUTH_CLIENT_BY_JWT_KEY_EXPIERD(HttpStatus.NOT_FOUND, "TOKEN IS EXPIRED"),
    AUTH_CLIENT_BY_JWT_NOT_SUPPORT(HttpStatus.NOT_FOUND, "TOKEN IS NOT OURS"),
    AUTH_CLIENT_BY_JWT_KEY_INVALID(HttpStatus.NOT_FOUND, "TOKEN IS WRONG"),
    AUTH_CLIENT_BY_AUTH_PERMISSION_TO_ACCESS_THE_REQUEST_ROLE(HttpStatus.NOT_FOUND, "NO PERMISSION"),
    AUTH_CLIENT_BY_AUTHORIZATION_IS_NECESSARY(HttpStatus.NOT_FOUND, "AUTHENTICATION IS REQUIRED"),

    /* CHAT */
    /* PLAYER */
    /* PLAYER LOG*/
    /* ROOM */
    ROOM_NOT_EXIST(HttpStatus.NOT_FOUND, "ROOM IS NOT FOUND"),
    ROOM_MODE_NOT_EXIST(HttpStatus.NOT_FOUND, "ROOM MODE IS NOT FOUND"),
    ROOM_ALREADY_EXIST(HttpStatus.NOT_FOUND, "ROOM IS ALREADY EXIST"),
    ROOM_PASSWORD_IS_WRONG(HttpStatus.NOT_FOUND, "ROOM PASSWORD IS WRONG"),

    /* ROOM LOG*/
    /* ROUND */
    API_ERROR_ROOM_NOT_EXIST(HttpStatus.NOT_FOUND, "Room is not exist"),
    ;

    ErrorInfo(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    private final HttpStatus httpStatus;
    private final String message;

}
