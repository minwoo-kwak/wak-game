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
    private Long roomId;
    private RedisUtil redisUtil;
    private ObjectMapper objectMapper;
    private RankService rankService;
    private final SocketUtil socketUtil;
    private final RoundService roundService;
    private final PlayerService playerService;
    private final RankFacade rankFacade;
    private final RoundFacade roundFacade;

    //todo: 너무 많은 것들을 주입받는데..? facade를 주입받을지 service를 주입받을지도 생각해야겠다
    public ClickEventProcessor(Long roomId, RedisUtil redisUtil, ObjectMapper objectMapper, RankService rankService, SocketUtil socketUtil, RoundService roundService, PlayerService playerService, RankFacade rankFacade, RoundFacade roundFacade) {
        this.roomId = roomId;
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
                // todo: room별로 저장하니까, 라운드가 끝나면 다음 라운드가 시작되기 전에 바로 db로 옮겨야 함.
                List<String> clickDataList = redisUtil.getListData("clicks:" + roomId.toString(), String.class);

                // todo: click 싹 가져오고.. 그 다음에 가져올때는 이전에꺼를 가져오면 안되잖아.. 그렇다고 하나 처리하고 바로 db에 저장하기에는 너무 오래걸리는데? 우짜누..
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

        /*if (!click.roundId().equals(this.roundId))
            throw new BusinessException(ErrorInfo.THREAD_ID_IS_DIFFERENT);*/

        String key = "roundId:" + click.roundId() + ":users";
        Map<String, PlayerInfo> data = redisUtil.getData(key, PlayerInfo.class);

        PlayerInfo user = data.get(Long.toString(click.userId()));
        PlayerInfo victim = data.get(Long.toString(click.victimId()));

        if (isAlive(user) && isAlive(victim)) {
            victim.updateStamina(-1);
            redisUtil.saveData(key, Long.toString(victim.getUserId()), victim);

            //게임 필드
            Round round = roundService.findById(roomId);
            List<PlayerInfoResponse> playersInfo = playerService.getPlayersInfo(round);
            socketUtil.sendMessage("/games/" + roomId.toString() + "/battle-field", playersInfo);

            //대시보드
            DashBoardResponse result = roundFacade.getDashBoard(roomId);
            socketUtil.sendMessage("/games/" + roomId.toString() + "dashboard", result);

            /*
               todo
                1R 종료 기준: 생존자 / 참여자 = 1/2
                1R 종료 기준: 생존자 / 참여자 = 1/4
                1. 몇 라운드인지 확인 후
                2. 기준 적용해서 라운드 끝났는지 확인
                3. 라운드 끝내기
             */

            //킬로그
            saveSuccessfulClick(click);
            socketUtil.sendMessage("/games" + roomId.toString() + "/kill-log", new KillLogResponse(click.roundId(), user.getNicKName(), user.getColor(), victim.getNicKName(), victim.getColor()));

            //랭킹
            rankService.updateRankings(click);
            rankFacade.sendRank(roomId);
        }
    }

    private boolean isAlive(PlayerInfo user) {
        return user.getStamina() > 0;
    }

    private void saveSuccessfulClick(clickVO click) {
        String key = "roundId:" + roomId.toString() + ":availableClicks";

        try {
            String clickData = objectMapper.writeValueAsString(click);
            redisUtil.saveToList(key, clickData);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing clickVO", e);
        }
    }
}
