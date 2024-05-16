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
import com.wak.game.domain.round.dto.PlayerCount;
import com.wak.game.global.error.ErrorInfo;
import com.wak.game.global.error.exception.BusinessException;
import com.wak.game.global.util.RedisUtil;
import com.wak.game.global.util.SocketUtil;

import java.util.List;
import java.util.Map;

public class ClickEventProcessor implements Runnable {
    private volatile boolean running = true;
    private Long roomId;
    private Long roundId;
    private long lastProcessedIndex = 0;
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
                List<String> clickDataList = redisUtil.getListData("clicks:" + roomId.toString(), String.class);
                for (long i = lastProcessedIndex; i < clickDataList.size(); i++) {
                    String clickData = clickDataList.get((int) i);
                    clickVO click = objectMapper.readValue(clickData, clickVO.class);
                    if (click != null) {
                        checkClickedUser(click);
                        lastProcessedIndex = i + 1;
                    }
                }
                Thread.sleep(10); // 1초 대기
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                throw new RuntimeException("Error processing click data", e);
            }
        }
    }


    private void checkClickedUser(clickVO click) {
        if (!click.roundId().equals(this.roundId))
            throw new BusinessException(ErrorInfo.THREAD_ID_IS_DIFFERENT);

        String key = "roundId:" + click.roundId() + ":users";
        Map<String, PlayerInfo> data = redisUtil.getData(key, PlayerInfo.class);

        PlayerInfo user = data.get(Long.toString(click.userId()));
        PlayerInfo victim = data.get(Long.toString(click.victimId()));

        if (isAlive(user) && isAlive(victim)) {
            victim.updateStamina(-1);
            redisUtil.saveData(key, Long.toString(victim.getUserId()), victim);

            // 게임 필드 업데이트
            Round round = roundService.findById(click.roundId());
            List<PlayerInfoResponse> playersInfo = playerService.getPlayersInfo(round);
            socketUtil.sendMessage("/games/" + roundId.toString() + "/battle-field", playersInfo);

            // 대시보드 업데이트
            DashBoardResponse result = roundFacade.getDashBoard(round.getId());
            socketUtil.sendMessage("/games/" + roundId.toString() + "/dashboard", result);

            // 생존자 수 업데이트
            String countKey = "aliveAndTotalPlayers";
            Map<String, PlayerCount> playerCountMap = redisUtil.getData(countKey, PlayerCount.class);
            PlayerCount playerCount = playerCountMap.get(roundId.toString());

            // 킬 로그 업데이트
            saveSuccessfulClick(click);
            socketUtil.sendMessage("/games" + roundId.toString() + "/kill-log", new KillLogResponse(click.roundId(), user.getNicKName(), user.getColor(), victim.getNicKName(), victim.getColor()));

            // 랭킹 업데이트
            rankFacade.updateRankings(click);
            rankFacade.sendRank(roundId);

            int roundNumber = round.getRoundNumber();
            boolean shouldEndRound = false;

            if (roundNumber < 3) {
                shouldEndRound = (double) playerCount.getAliveCountA() / playerCount.getTotalCountA() <= 0.5;
            } else if (roundNumber == 3) {
                shouldEndRound = playerCount.getAliveCountA() == 1 || playerCount.getAliveCountB() == 1;
            }

            if (shouldEndRound) {
                socketUtil.sendMessage("/games/" + roundId.toString() + "/battle-field", roundNumber + " ROUND FINISH!");

                roundFacade.endRound(round.getId());
                countDown(60);

                if (roundNumber < 3) {
                    Round nextRound = roundFacade.startNextRound(round);
                    updateRoundId(nextRound.getId());
                } else {
                    stop();
                }
            }
        }
    }

    private void processClickLogsAndCleanup(Long roundId, Long roomId) {
        try {
            List<String> clickDataList = redisUtil.getListData("clicks:" + roomId.toString(), String.class);
            for (String clickData : clickDataList) {
                clickVO click = objectMapper.readValue(clickData, clickVO.class);
                if (click != null) {
                    // 클릭 로그를 DB에 저장하는 로직
                    saveClickLogToDB(click);
                }
            }
            // Redis에서 클릭 로그 제거
            redisUtil.deleteKey("clicks:" + roomId.toString());
        } catch (JsonProcessingException e) {
            System.out.println(e);
        }
    }

    private void countDown(int sec) {
        new Thread(() -> {
            try {
                for (int i = sec; i > 0; i--) {
                    socketUtil.sendMessage("/games/" + roundId.toString() + "/battle-field", "Round will start in " + i + " seconds");
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();

        socketUtil.sendMessage("/games/" + roomId.toString() + "/battle-field", "Game Start!");
    }

    private boolean isAlive(PlayerInfo user) {
        return user.getStamina() > 0;
    }

    private void saveSuccessfulClick(clickVO click) {
        String key = "roundId:" + roomId.toString() + ":availableClicks";
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
