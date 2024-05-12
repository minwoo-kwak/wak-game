package com.wak.game.domain.round.thread;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wak.game.application.facade.RankFacade;
import com.wak.game.application.facade.RoundFacade;
import com.wak.game.application.response.DashBoardResponse;
import com.wak.game.application.response.PlayerInfoResponse;
import com.wak.game.application.response.socket.KillLogResponse;
import com.wak.game.application.vo.clickVO;
import com.wak.game.domain.player.Player;
import com.wak.game.domain.player.PlayerService;
import com.wak.game.domain.player.dto.PlayerInfo;
import com.wak.game.domain.rank.RankService;
import com.wak.game.domain.round.Round;
import com.wak.game.domain.round.RoundService;
import com.wak.game.global.error.ErrorInfo;
import com.wak.game.global.error.exception.BusinessException;
import com.wak.game.global.util.RedisUtil;
import com.wak.game.global.util.SocketUtil;

import java.util.List;
import java.util.Map;

public class ClickEventProcessor implements Runnable {
    private volatile boolean running = true;
    private Long roundId;
    private RedisUtil redisUtil;
    private ObjectMapper objectMapper;
    private RankService rankService;
    private final SocketUtil socketUtil;
    private final RoundService roundService;
    private final PlayerService playerService;
    private final RankFacade rankFacade;
    private final RoundFacade roundFacade;

    public ClickEventProcessor(Long roundId, RedisUtil redisUtil, ObjectMapper objectMapper, RankService rankService, SocketUtil socketUtil, RoundService roundService, PlayerService playerService, RankFacade rankFacade, RoundFacade roundFacade) {
        this.roundId = roundId;
        this.redisUtil = redisUtil;
        this.objectMapper = objectMapper;
        this.rankService = rankService;
        this.socketUtil = socketUtil;
        this.roundService = roundService;
        this.playerService = playerService;
        this.rankFacade = rankFacade;
        this.roundFacade = roundFacade;
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

            //게임 필드
            Round round = roundService.findById(roundId);
            List<PlayerInfoResponse> playersInfo = playerService.getPlayersInfo(round);
            socketUtil.sendMessage("/games/" + roundId.toString() + "/battle-field", playersInfo);

            //대시보드
            DashBoardResponse result = roundFacade.getDashBoard(roundId);
            socketUtil.sendMessage("/games/" + roundId.toString() + "dashboard", result);

            //킬로그
            saveSuccessfulClick(click);
            socketUtil.sendMessage("/games" + roundId.toString() + "/kill-log", new KillLogResponse(click.roundId(), user.getNicKName(), user.getColor(), victim.getNicKName(), victim.getColor()));

            //랭킹
            rankService.updateRankings(click);
            rankFacade.sendRank(roundId);
        }
    }

    private boolean isAlive(PlayerInfo user) {
        return user.getStamina() > 0;
    }

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
