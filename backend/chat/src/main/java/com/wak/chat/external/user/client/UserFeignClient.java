package com.wak.chat.external.user.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import com.wak.chat.external.user.dto.UserInfoResponse;
import com.wak.chat.global.util.ApiResult;

@FeignClient(name = "userApi", url = "https://wakgame.com/api")
public interface UserFeignClient {

	@GetMapping("/users/info")
	ApiResult<UserInfoResponse> getUserInfo(@RequestHeader("Authorization") String token);

}
