package com.wak.game.domain;

import com.wak.game.global.error.ErrorInfo;
import com.wak.game.global.error.exception.BusinessException;
import com.wak.game.global.util.ApiResult;
import com.wak.game.global.util.ApiUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class TestService {

    public ResponseEntity<ApiResult<?>> throwException() {

        if (true) {
            throw new BusinessException(ErrorInfo.TEST);
        }

        return ResponseEntity.ok(ApiUtils.success(null));
    }

}
