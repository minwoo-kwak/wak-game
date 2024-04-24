package com.wak.game.global.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

public class ApiUtils {

    public static <T> ApiResult<T> success(T data) {
        return new ApiResult<>(true, data, null);
    }

    public static ApiResult<?> error(String errorResponse) { // todo set type of the error response
        return new ApiResult<>(false, null, errorResponse);
    }

    @Getter
    @ToString
    @AllArgsConstructor
    public static class ApiResult<T> {

        private final boolean success;
        private final T data;
        private final String error;

    }

}
