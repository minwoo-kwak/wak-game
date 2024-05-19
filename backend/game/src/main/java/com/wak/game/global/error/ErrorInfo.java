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
    PLAYER_NOT_FOUND(HttpStatus.NOT_FOUND, "PLAYER IS NOT FOUND"),
    PLAYER_NOT_WINNER(HttpStatus.FORBIDDEN, "PLAYER IS NOT WINNER"),

    /* PLAYER LOG*/
    ClICK_LOG_IS_EMPTY(HttpStatus.NOT_FOUND, "CLICK LOG IS EMPTY"),

    /* ROOM */
    ROOM_NOT_EXIST(HttpStatus.NOT_FOUND, "ROOM IS NOT FOUND"),
    ROOM_NOT_EXIST_IN_REDIS(HttpStatus.NOT_FOUND, "ROOM IS NOT FOUND IN REDIS"),
    ROOM_MODE_NOT_EXIST(HttpStatus.NOT_FOUND, "ROOM MODE IS NOT FOUND"),
    ROOM_ALREADY_EXIST(HttpStatus.NOT_FOUND, "ROOM IS ALREADY EXIST"),
    ROOM_PASSWORD_IS_WRONG(HttpStatus.NOT_FOUND, "ROOM PASSWORD IS WRONG"),
    ROOM_PLAYER_IS_FULL(HttpStatus.NOT_FOUND, "ROOM IS FULL"),
    ROOM_PLAYER_IS_EMPTY(HttpStatus.NOT_FOUND, "ROOM IS EMPTY"),
    ROOM_USER_ALREADY_EXIST(HttpStatus.NOT_FOUND, "USER IS ALREADY EXIST IN ROOM"),
    ROOM_USER_NOT_EXIST(HttpStatus.NOT_FOUND, "USER IS NOT EXIST IN ROOM"),
    ROOM_IS_START(HttpStatus.NOT_FOUND, "ROOM IS START"),
    ROOM_ALREADY_ENDED(HttpStatus.NOT_FOUND, "ROOM IS END"),

    ROOM_ALREADY_STARTED(HttpStatus.CONFLICT,"ROOM IS ALREADY IN GAME"),
    ROOM_NOT_HOST(HttpStatus.FORBIDDEN, "DO NOT HAVE PERMISSION TO START THE GAME"),
    /* ROOM LOG*/
    /* ROUND */
    ROUND_NOT_EXIST(HttpStatus.NOT_FOUND, "ROUND IS NOT FOUND"),
    ROUND_NOT_MATCHED(HttpStatus.CONFLICT, "ROUND IS NOT MATCHED"),

    /* THREAD */
    THREAD_ID_IS_DIFFERENT(HttpStatus.NOT_FOUND, "현재 동작하는 스레드와 처리하려는 데이터의 roundId가 다릅니다."),/*나중에 지울겠습니다*/
    THREAD_DESERIALIZING_DATA(HttpStatus.BAD_REQUEST, "DATA DESERIALIZING FAIL"),
    THREAD_SERIALIZING_DATA(HttpStatus.BAD_REQUEST, "DATA SERIALIZING FAIL"),
    THREAD_FORMAT_NOT_MATCHED(HttpStatus.BAD_REQUEST, "DATA FORMAT IS NOT MATCHED")
    /**/;

    ErrorInfo(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    private final HttpStatus httpStatus;
    private final String message;

}
