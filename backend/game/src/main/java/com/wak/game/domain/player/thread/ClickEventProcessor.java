package com.wak.game.domain.player.thread;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wak.game.application.vo.clickVO;
import com.wak.game.domain.player.dto.PlayerInfo;
import com.wak.game.domain.round.Round;
import com.wak.game.domain.round.RoundService;
import com.wak.game.global.error.ErrorInfo;
import com.wak.game.global.error.exception.BusinessException;
import com.wak.game.global.util.RedisUtil;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ClickEventProcessor implements Runnable {
    private volatile boolean running = true;
    private Long roundId;
    private RedisUtil redisUtil;
    private ObjectMapper objectMapper;
    private RoundService roundService;


    public ClickEventProcessor(Long roundId, RedisUtil redisUtil, ObjectMapper objectMapper, RoundService roundService) {
        this.roundId = roundId;
        this.redisUtil = redisUtil;
        this.objectMapper = objectMapper;
        this.roundService = roundService;
    }

    @Override
    public void run() {
        while (running) {
            try {
                List<String> clickDataList = redisUtil.getListData("clicks:" + roundId, String.class);
                for (String clickData : clickDataList) {
                    clickVO click = objectMapper.readValue(clickData, clickVO.class);
                    if (click != null)
                        handleClickedUser(click);
                }

                Thread.sleep(1000); // 1초 대기
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (Exception e) { // 포괄적인 예외 처리
                throw new RuntimeException("Error processing click data", e);
            }
        }
    }

    private void handleClickedUser(clickVO click) {

        if (!click.roundId().equals(this.roundId))
            throw new BusinessException(ErrorInfo.THREAD_ID_IS_DIFFERENT);

        String key = "roundId:" + click.roundId() + ":users";
        Long userId = click.userId();
        Long victimId = click.victimId();

        Map<String, PlayerInfo> data = redisUtil.getData(key, PlayerInfo.class);

        if (data.get(Long.toString(userId)).getStamina() > 0 && data.get(Long.toString(victimId)).getStamina() > 0) {

            PlayerInfo victim = data.get(Long.toString(victimId));

            victim.updateStamina(-1);
            redisUtil.saveData(key, Long.toString(victimId), victim);
        }

    }
}
