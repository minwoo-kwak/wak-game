package com.wak.game.application.controller;

import com.wak.game.global.util.CustomMockMvcSpringBootTest;
import com.wak.game.global.util.RequestUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

@CustomMockMvcSpringBootTest
class TestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    RequestUtil requestUtil;

    @Test
    void withPathVariableAndJsonRequestAndJsonResponse() throws Exception {

        int pathVariable = 708;
        String queryParam = "2";

        // 요청에 포함된 JSON
        TestController.TestRequestDto request = new TestController.TestRequestDto("Hello", "My name is Hans");

        // 내가 예상하는 응답 data
        TestController.TestResponseDto expectedResponse  = new TestController.TestResponseDto("Hello", "My name is Hans", pathVariable + Integer.parseInt(queryParam));

        mockMvc
            .perform(
                requestUtil
                        .getRequestWithJson("/api/test/{pathVariable}", request, pathVariable) // URL, Body, PathVariable
//                        .getRequest("/api/test/{pathVariable}", pathVariable) // 바디로 JSON 데이터가 오지않는 경우
                        .param("queryParam", queryParam) // 쿼리 파라미터
            )
            .andExpectAll(
                requestUtil.status().isOk(), // 내가 예상하는 상태 코드
                requestUtil.jsonContent(expectedResponse)
            );
    }

}