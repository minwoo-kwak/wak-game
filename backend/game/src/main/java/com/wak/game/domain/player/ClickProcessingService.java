package com.wak.game.domain.player;

import com.wak.game.application.vo.clickVO;
import com.wak.game.global.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClickProcessingService {

    private final RedisUtil redisUtil;
    @Async
    public void processClickEvents(Long roundId) {
        /*while (!Thread.currentThread().isInterrupted()) {
            // Redis 리스트에서 가장 오래된 클릭 로그 하나를 가져온다
            String clickData = redisTemplate.opsForList().leftPop(key, 0, TimeUnit.SECONDS);

            if (clickData != null) {
                try {
                    // JSON 문자열을 clickVO 객체로 변환
                    clickVO click = objectMapper.readValue(clickData, clickVO.class);
                    // 클릭 처리 로직 수행
                    handleClickedUser(click);
                } catch (IOException e) {
                    System.err.println("Error processing click data: " + e.getMessage());
                }
            }

            // 일정 시간 대기하며 폴링 간격 조절
            try {
                Thread.sleep(1000); // 1초 대기
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // 인터럽트 발생 시 스레드 인터럽트 상태를 설정하여 while 루프 종료
            }
        }*/
    }

    private void handleClickedUser(clickVO click) {
        // 클릭에 대한 실제 처리 로직
        System.out.println("Processed click from user " + click.userId());
        //redisUtil.delete(//해당 벨류)
    }
}

