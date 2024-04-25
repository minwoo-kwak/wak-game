package com.wak.game.global.util;

public class ApiUtils {

    public static <T> ApiResult<T> success(T data) {
        return new ApiResult<>(true, data, null);
    }

    public static ApiResult<?> error(ApiError errorResponse) { // todo set type of the error response
        return new ApiResult<>(false, null, errorResponse);
    }

}
