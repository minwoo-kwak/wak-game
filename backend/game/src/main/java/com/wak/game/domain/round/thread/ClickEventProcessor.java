package com.wak.game.domain.round.thread;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wak.game.application.facade.RankFacade;
import com.wak.game.application.facade.RoundFacade;
import com.wak.game.application.response.DashBoardResponse;
import com.wak.game.application.response.PlayerInfoResponse;
import com.wak.game.application.response.socket.KillLogResponse;
import com.wak.game.application.response.socket.ResultResponse;
import com.wak.game.application.response.socket.RoundEndResultResponse;
import com.wak.game.application.vo.RoomVO;
import com.wak.game.application.vo.clickVO;
import com.wak.game.domain.player.Player;
import com.wak.game.domain.player.PlayerService;
import com.wak.game.domain.player.dto.PlayerInfo;
import com.wak.game.domain.rank.RankService;
import com.wak.game.domain.rank.dto.RankInfo;
import com.wak.game.domain.round.Round;
import com.wak.game.domain.round.RoundService;
import com.wak.game.domain.round.dto.PlayerCount;
import com.wak.game.global.error.ErrorInfo;
import com.wak.game.global.error.exception.BusinessException;
import com.wak.game.global.util.RedisUtil;
import com.wak.game.global.util.SocketUtil;

import javax.xml.transform.Result;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ClickEventProcessor implements Runnable {
    private volatile boolean running = true;
    private long roomId;
    private Long roundId;
    private int lastProcessedIndex = 0;
    private RedisUtil redisUtil;
    private ObjectMapper objectMapper;
    private final SocketUtil socketUtil;
    private final RoundService roundService;
    private final PlayerService playerService;
    private final RoundFacade roundFacade;
    private final RankFacade rankFacade;

    public ClickEventProcessor(Long roundId, Long roomId, RedisUtil redisUtil, ObjectMapper objectMapper, SocketUtil socketUtil, RoundService roundService, PlayerService playerService, RoundFacade roundFacade, RankFacade rankFacade) {
        this.roundId = roundId;
        this.roomId = roomId;
        this.redisUtil = redisUtil;
        this.objectMapper = objectMapper;
        this.socketUtil = socketUtil;
        this.roundService = roundService;
        this.playerService = playerService;
        this.roundFacade = roundFacade;
        this.rankFacade = rankFacade;
    }

    @Override
    public void run() {
        countDown(3);

        while (running) {
            try {
                List<clickVO> clickDataList = redisUtil.getListData("roomId:" + roomId + ":clicks", clickVO.class);

                for (int i = lastProcessedIndex; i < clickDataList.size(); i++) {
                    clickVO click = clickDataList.get(i);
                    if (click != null) {
                        checkClickedUser(click);
                        lastProcessedIndex = i + 1;
                    }
                }

                Thread.sleep(1000); // 10밀리초 대기
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void checkClickedUser(clickVO click) {
        if (!click.roundId().equals(this.roundId))
            throw new BusinessException(ErrorInfo.THREAD_ID_IS_DIFFERENT);

        String key = "roomId:" + roomId + ":users";
        Map<String, PlayerInfo> data = redisUtil.getData(key, PlayerInfo.class);

        PlayerInfo user = data.get(click.userId().toString());
        PlayerInfo victim = data.get(Long.toString(click.victimId()));


        if (isAlive(user) && isAlive(victim)) {
            Round round = roundService.findById(click.roundId());

            victim.updateStamina(-1);
            redisUtil.saveData(key, Long.toString(victim.getUserId()), victim);

            roundFacade.sendBattleField(roomId, false);
            roundFacade.sendDashBoard(roomId, round.getRoundNumber());

            // 생존자 수 업데이트
            String countKey = "aliveAndTotalPlayers";
            Map<String, PlayerCount> playerCountMap = redisUtil.getData(countKey, PlayerCount.class);
            PlayerCount playerCount = playerCountMap.get(Long.toString(roomId));
            playerCount.updateAliveCont();
            redisUtil.saveData(countKey, Long.toString(roomId), playerCount);

            saveSuccessfulClick(click);
            socketUtil.sendMessage("/games/" + roomId + "/kill-log", new KillLogResponse(click.roundId(), user.getNickname(), user.getColor(), victim.getNickname(), victim.getColor()));

            rankFacade.updateRankings(click, roomId);
            rankFacade.sendRank(roomId);

            countDown(60);

            playerCountMap = redisUtil.getData(countKey, PlayerCount.class);
            playerCount = playerCountMap.get(roundId.toString());

            if (playerCount.getAliveCountA() > 1)
                return;

            if (round.getRoundNumber() == 3) {
                sendResult(null);
                roundFacade.endRound(roomId);
                stop();
            }

            Round nextRound = roundFacade.startNextRound(round);
            sendResult(nextRound.getId());

            roundFacade.endRound(roomId);
            updateRoundId(nextRound.getId());
        }
    }

    private void sendResult(long nextRoundId) {
        String key = "roomId:" + roomId + ":users";
        Round round = roundService.findById(roundId);

        Map<String, RankInfo> ranks = redisUtil.getData("roomId:" + roomId + ":ranks", RankInfo.class);
        Map<String, PlayerInfo> playersMap = redisUtil.getData(key, PlayerInfo.class);

        List<RankInfo> sortedRanks = new ArrayList<>(ranks.values());
        sortedRanks.sort((r1, r2) -> Integer.compare(r2.getKillCnt(), r1.getKillCnt()));

        List<ResultResponse> results = new ArrayList<>();
        int rank = 1;

        for (RankInfo rankInfo : sortedRanks) {
            long userId = rankInfo.getUserId();
            PlayerInfo player = playersMap.get(Long.toString(userId));
            if (player != null) {
                ResultResponse result = new ResultResponse(
                        userId,
                        rank++,
                        rankInfo.getKillCnt()
                );
                results.add(result);
            }
        }

        socketUtil.sendMessage("/games/" + roomId + "/battle-field", new RoundEndResultResponse(true, round.getRoundNumber(), nextRoundId, results));
    }

    private void countDown(int sec) {
        new Thread(() -> {
            try {
                for (int i = sec; i > 0; i--) {
                    socketUtil.sendMessage("/games/" + roomId + "/battle-field", "Round will start in " + i + " seconds");
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();

        socketUtil.sendMessage("/games/" + roomId + "/battle-field", "Game Start!");
    }

    private boolean isAlive(PlayerInfo user) {
        return user.getStamina() > 0;
    }

    private void saveSuccessfulClick(clickVO click) {
        String key = "roomId:" + roomId + ":availableClicks";

        try {
            String clickData = objectMapper.writeValueAsString(click);
            redisUtil.saveToList(key, clickData);
        } catch (Exception e) {
            throw new RuntimeException("Error serializing clickVO", e);
        }
    }

    public void stop() {
        running = false;
    }

    private void updateRoundId(Long newRoundId) {
        this.roundId = newRoundId;
    }

}
