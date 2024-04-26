package com.wak.game.application.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wak.game.global.util.ApiUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/{pathVariable}")
    public ResponseEntity<?> test(@RequestBody TestRequestDto requestDto, @RequestParam Integer queryParam, @PathVariable Integer pathVariable) {

        TestResponseDto responseDto = new TestResponseDto(requestDto.title, requestDto.content, pathVariable + queryParam);

        return ResponseEntity.ok(ApiUtils.success(responseDto));
    }

    @AllArgsConstructor
    @Getter
    public static class TestRequestDto {

        private String title;
        private String content;

    }

    @AllArgsConstructor
    @Getter
    public static class TestResponseDto {

        private String title;
        private String content;
        private int testNum;

        public TestResponseDto(String title, String content) {
            this.title = title;
            this.content = content;
        }
    }

}
