package com.wak.game.domain.round.thread;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wak.game.application.vo.clickVO;
import com.wak.game.domain.player.dto.PlayerInfo;
import com.wak.game.domain.rank.RankService;
import com.wak.game.global.error.ErrorInfo;
import com.wak.game.global.error.exception.BusinessException;
import com.wak.game.global.util.RedisUtil;

import java.util.List;
import java.util.Map;

public class ClickEventProcessor implements Runnable {
    private volatile boolean running = true;
    private Long roundId;
    private RedisUtil redisUtil;
    private ObjectMapper objectMapper;
    private RankService rankService;

    public ClickEventProcessor(boolean running, Long roundId, RedisUtil redisUtil, ObjectMapper objectMapper, RankService rankService) {
        this.running = running;
        this.roundId = roundId;
        this.redisUtil = redisUtil;
        this.objectMapper = objectMapper;
        this.rankService = rankService;
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
            } catch (Exception e) {
                throw new RuntimeException("Error processing click data", e);
            }
        }
    }

    private void handleClickedUser(clickVO click) {

        if (!click.roundId().equals(this.roundId))
            throw new BusinessException(ErrorInfo.THREAD_ID_IS_DIFFERENT);

        String key = "roundId:" + click.roundId() + ":users";
        Map<String, PlayerInfo> data = redisUtil.getData(key, PlayerInfo.class);

        PlayerInfo user = data.get(Long.toString(click.userId()));
        PlayerInfo victim = data.get(Long.toString(click.victimId()));

        if (isAlive(user) && isAlive(victim)) {
            victim.updateStamina(-1);
            redisUtil.saveData(key, Long.toString(victim.getUserId()), victim);

            rankService.updateRankings(click);
            saveSuccessfulClick(click);
        }

    }

    private boolean isAlive(PlayerInfo user) {
        return user.getStamina() > 0;
    }

    /**
     * 실시간 랭킹 조회하기 위해서 킬 성공했으면 랭킹 관련 레디스에 추가
     * 랭킹: HashMap<roundId, HashMap<userId,킬 수>> 형태로 저장
     *
     * @param click
     */
    private void updateRankings(clickVO click) {
        Long userId = click.userId();
        Long roundId = click.roundId();
        Map<String, Integer> curRoundRanks = redisUtil.getData("roundId:" + roundId + ":rankings", Integer.class);

        int curKillCnt = curRoundRanks.getOrDefault(Long.toString(userId), 0);
        curRoundRanks.put(Long.toString(userId), curKillCnt + 1);

        redisUtil.saveData("roundId:" + roundId + ":rankings", Long.toString(userId), curRoundRanks);
    }

    /**
     * 성공 클릭 저장
     * 성공 클릭: HashMap<key,List<toString(clickVo)>>
     * list에 추가하기
     *
     * @param click
     */
    private void saveSuccessfulClick(clickVO click) {
        String key = "roundId:" + roundId + ":availableClicks";

        try {
            String clickData = objectMapper.writeValueAsString(click);
            redisUtil.saveToList(key, clickData);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing clickVO", e);
        }
    }
}
