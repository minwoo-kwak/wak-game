package com.wak.game.domain.player;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wak.game.application.vo.clickVO;
import com.wak.game.global.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class ClickProcessingService {

    private final RedisUtil redisUtil;
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    @Async
    public void processClickEvents(String key) {

        while (!Thread.currentThread().isInterrupted()) {
            try {
                // Redis에서 가장 오래된 클릭 로그를 가져옵니다
                String clickData = redisTemplate.opsForList().leftPop(key, 0, TimeUnit.SECONDS);
                clickVO click = convertToVO(clickData, clickVO.class);

                if(click != null)
                    handleClickedUser(click);

                Thread.sleep(1000); // 1초 대기
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }

    }

    private clickVO convertToVO(String clickData, Class<clickVO> clickVOClass) throws JsonProcessingException {
        if (clickData != null) {
            clickVO click = objectMapper.readValue(clickData, clickVO.class);
            handleClickedUser(click);
            return click;
        }
        return null;
    }

    private void handleClickedUser(clickVO click) {
        //클릭에 대한 실제 처리 로직
    }
}

