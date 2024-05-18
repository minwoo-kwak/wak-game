package com.wak.game.domain.round.thread;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wak.game.application.facade.RankFacade;
import com.wak.game.application.facade.RoundFacade;
import com.wak.game.application.response.socket.FinalResultResponse;
import com.wak.game.application.response.socket.KillLogResponse;
import com.wak.game.application.response.socket.ResultResponse;
import com.wak.game.application.response.socket.RoundEndResultResponse;
import com.wak.game.application.vo.clickVO;
import com.wak.game.domain.player.Player;
import com.wak.game.domain.player.PlayerService;
import com.wak.game.domain.player.dto.PlayerInfo;
import com.wak.game.domain.round.Round;
import com.wak.game.domain.round.RoundService;
import com.wak.game.domain.user.User;
import com.wak.game.domain.user.UserService;
import com.wak.game.global.error.ErrorInfo;
import com.wak.game.global.error.exception.BusinessException;
import com.wak.game.global.util.RedisUtil;
import com.wak.game.global.util.SocketUtil;
import com.wak.game.global.util.TimeUtil;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class ClickEventProcessor implements Runnable {
    private volatile boolean running = true;
    private final Long roomId;
    private Long roundId;
    private Long round1Id;
    private Long round2Id;
    private Long round3Id;
    private final int playerCount;
    private int aliveCount;
    private int lastProcessedIndex = 0;
    private RedisUtil redisUtil;
    private ObjectMapper objectMapper;
    private final SocketUtil socketUtil;
    private final RoundService roundService;
    private final PlayerService playerService;
    private final UserService userService;
    private final RoundFacade roundFacade;
    private final RankFacade rankFacade;
    private final TimeUtil timeUtil;

    public ClickEventProcessor(Long roundId, Long roomId, int playerCnt, RedisUtil redisUtil, ObjectMapper objectMapper, SocketUtil socketUtil, RoundService roundService, PlayerService playerService, RoundFacade roundFacade, RankFacade rankFacade, UserService userService, TimeUtil timeUtil) {
        this.roundId = roundId;
        this.round1Id = roundId;
        this.roomId = roomId;
        this.playerCount = playerCnt;
        this.redisUtil = redisUtil;
        this.objectMapper = objectMapper;
        this.socketUtil = socketUtil;
        this.roundService = roundService;
        this.playerService = playerService;
        this.roundFacade = roundFacade;
        this.rankFacade = rankFacade;
        this.userService = userService;
        this.timeUtil= timeUtil;
    }

    @Override
    public void run() {
        //countDown(3);

        while (running) {
            try {
                List<clickVO> clickDataList = redisUtil.getListData("roomId:" + roomId + ":clicks", clickVO.class);

                for (int i = lastProcessedIndex; i < clickDataList.size(); i++) {
                    clickVO click = clickDataList.get(i);
                    if (click != null) {
                        System.out.println(click.userId() + "의 공격 처리!");
                        checkClickedUser(click);
                        lastProcessedIndex++;
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
        PlayerInfo victim = data.get(click.victimId().toString());


        if (isAlive(user) && isAlive(victim)) {
            Round round = roundService.findById(click.roundId());

            victim.updateStamina(-1);
            redisUtil.saveData(key, victim.getUserId().toString(), victim);

            roundFacade.sendBattleField(roomId, false);
            roundFacade.sendDashBoard(roomId, round.getRoundNumber());

            // 생존자 수 업데이트
            --aliveCount;

            saveSuccessfulClick(click);
            socketUtil.sendMessage("/games/" + roomId + "/kill-log", new KillLogResponse(click.roundId(), user.getNickname(), user.getColor(), victim.getNickname(), victim.getColor()));

            rankFacade.updateRankings(click, roomId);
            rankFacade.sendRank(roomId);

            //countDown(60);

            if (aliveCount > 1)
                return;

            if (round.getRoundNumber() == 3) {
                sendResult(null);
                stop();
            }

            Round nextRound = roundFacade.startNextRound(round);
            sendResult(nextRound.getId());

            roundFacade.endRound(roomId, roundId);
            updateNextRound(nextRound.getId());
        }
    }

    private void sendResult(Long nextRoundId) {
        Round round = roundService.findById(roundId);
        List<ResultResponse> results = new ArrayList<>();

        Map<Long, Player> playerMap = playerService.getPlayerMap(roundId);

        for (Player player : playerMap.values()) {
            Player murderPlayer = player.getMurderPlayer();
            String murderNickname = (murderPlayer != null) ? murderPlayer.getUser().getNickname() : null;
            String murderColor = (murderPlayer != null) ? murderPlayer.getUser().getColor().getHexColor() : null;

            results.add(new ResultResponse(
                    player.getUser().getId(),
                    player.getRank(),
                    player.getKillCount(),
                    timeUtil.nanoToDouble(Long.parseLong(player.getAliveTime())),
                    murderNickname,
                    murderColor
            ));
        }

        if (round.getRoundNumber() < 3) {
            socketUtil.sendMessage("/games/" + roomId + "/battle-field", new RoundEndResultResponse(true, round.getRoundNumber(), nextRoundId, results, null));
            return;
        }

        List<FinalResultResponse> finals = getFinalResult();
        socketUtil.sendMessage("/games/" + roomId + "/battle-field", new RoundEndResultResponse(true, round.getRoundNumber(), nextRoundId, results, finals));
    }

    private List<FinalResultResponse> getFinalResult() {

        List<FinalResultResponse> finalResults = new ArrayList<>();

        Map<Long, Player> playerR1Map = playerService.getPlayerMap(round1Id);//이거를 기준으로 한바퀴 돌면서
        Map<Long, Player> playerR2Map = playerService.getPlayerMap(round2Id);
        Map<Long, Player> playerR3Map = playerService.getPlayerMap(round3Id);

        Round round1 = roundService.findById(round1Id);
        Round round2 = roundService.findById(round2Id);
        Round round3 = roundService.findById(round3Id);

        int r1Time = (int) ChronoUnit.SECONDS.between(round1.getCreatedAt(), round1.getUpdatedAt());
        int r2Time = (int) ChronoUnit.SECONDS.between(round2.getCreatedAt(), round2.getUpdatedAt()) - 30;
        int r3Time = (int) ChronoUnit.SECONDS.between(round3.getCreatedAt(), round3.getUpdatedAt()) - 30;

        int totalGameTime = r1Time + r2Time + r3Time;

        for (Player playerR1 : playerR1Map.values()) {
            Long userId = playerR1.getUser().getId();

            Player playerR2 = playerR2Map.get(userId);
            Player playerR3 = playerR3Map.get(userId);

            int totalKillCount = playerR1.getKillCount() + (playerR2 != null ? playerR2.getKillCount() : 0) + (playerR3 != null ? playerR3.getKillCount() : 0);

            long totalAliveNanoTime = parseNanoTime(playerR1.getAliveTime())
                    + (playerR2 != null ? parseNanoTime(playerR2.getAliveTime()) : 0)
                    + (playerR3 != null ? parseNanoTime(playerR3.getAliveTime()) : 0);

            double totalAliveTime = timeUtil.nanoToDouble(totalAliveNanoTime);

            finalResults.add(new FinalResultResponse(userId,totalGameTime, totalAliveTime, totalKillCount));
        }

        finalResults.sort(Comparator.comparing(FinalResultResponse::getTotalKillCount).reversed()
                .thenComparing(FinalResultResponse::getTotalAliveTime));

        String winnerName=null;
        String winnerColor=null;
        for (int i = 0; i < finalResults.size(); i++) {
            if(i==0){
                winnerName= userService.findById(finalResults.get(i).getUserId()).toString();
                winnerColor= userService.findById(finalResults.get(i).getUserId()).getColor().getHexColor();
            }
            finalResults.get(i).updateRank(i + 1);
            finalResults.get(i).updateWinner(winnerName,winnerColor);
        }

        return finalResults;
    }

    private long parseNanoTime(String nanoTime) {
        String[] parts = nanoTime.split(":");
        long minutes = Long.parseLong(parts[0]);
        long seconds = Long.parseLong(parts[1]);
        return minutes * 60 * 1_000_000_000L + seconds * 1_000_000_000L;
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

    private void updateNextRound(Long newRoundId) {
        Round round = roundService.findById(roundId);

        if (round.getRoundNumber() == 3)
            return;

        if (round.getRoundNumber() == 1) {
            this.round2Id = newRoundId;
        }

        if (round.getRoundNumber() == 2) {
            this.round3Id = newRoundId;
        }
        this.roundId = newRoundId;
        this.aliveCount = playerCount;
    }

}
