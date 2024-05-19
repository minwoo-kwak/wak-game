package com.wak.game.domain.round.thread;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wak.game.application.facade.RankFacade;
import com.wak.game.application.facade.RoundFacade;
import com.wak.game.application.response.socket.FinalResultResponse;
import com.wak.game.application.response.socket.KillLogResponse;
import com.wak.game.application.response.socket.ResultResponse;
import com.wak.game.application.response.socket.RoundEndResultResponse;
import com.wak.game.domain.player.Player;
import com.wak.game.domain.player.PlayerService;
import com.wak.game.domain.player.dto.PlayerInfo;
import com.wak.game.domain.room.Room;
import com.wak.game.domain.round.Round;
import com.wak.game.domain.round.RoundService;
import com.wak.game.domain.round.dto.ClickDTO;
import com.wak.game.domain.user.User;
import com.wak.game.domain.user.UserService;
import com.wak.game.global.error.ErrorInfo;
import com.wak.game.global.error.exception.BusinessException;
import com.wak.game.global.util.RedisUtil;
import com.wak.game.global.util.SocketUtil;
import com.wak.game.global.util.TimeUtil;
import jakarta.transaction.Transactional;

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
        this.timeUtil = timeUtil;
    }

    @Override
    public void run() {
        System.out.println("스레드 시작");
        //countDown(3);

        while (running) {
            try {
                List<ClickDTO> clickDataList = redisUtil.getListData("roomId:" + roomId + ":clicks", ClickDTO.class);

                System.out.println("클릭 사이즈");
                System.out.println(clickDataList.size());
                System.out.println("마지막 인덱스: " + lastProcessedIndex);

                for (int i = lastProcessedIndex; i < clickDataList.size(); i++) {
                    ClickDTO click = clickDataList.get(i);
                    if (click != null) {
                        System.out.println(click.getUserId() + "의 공격 처리!");
                        checkClickedUser(click);
                        lastProcessedIndex++;
                    }

                    lastProcessedIndex++;
                }

                Thread.sleep(1000); // 10밀리초 대기
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void checkClickedUser(ClickDTO click) {
        if (!click.getRoundId().equals(this.roundId))
            throw new BusinessException(ErrorInfo.THREAD_ID_IS_DIFFERENT);

        String key = "roomId:" + roomId + ":users";
        System.out.println("(nullpointer)key: " + key);
        Map<String, PlayerInfo> data = redisUtil.getData(key, PlayerInfo.class);

        PlayerInfo user = data.get(click.getUserId().toString());
        PlayerInfo victim = data.get(click.getVictimId().toString());
        if (user == null) {
            System.out.println("공격자 정보 없음");
            throw new BusinessException(ErrorInfo.PLAYER_NOT_FOUND);
        }
        if (victim == null) {
            System.out.println("피해자 정보 없음");
            throw new BusinessException(ErrorInfo.PLAYER_NOT_FOUND);
        }

        if (isAlive(user) && isAlive(victim)) {
            Round round = roundService.findById(click.getRoundId());

            victim.updateStamina(-1);
            redisUtil.saveData(key, victim.getUserId().toString(), victim);

            roundFacade.sendBattleField(roomId, false);
            roundFacade.sendDashBoard(roomId, round.getRoundNumber());

            // 생존자 수 업데이트
            --aliveCount;

            saveSuccessfulClick(click);
            socketUtil.sendMessage("/games/" + roomId + "/kill-log", new KillLogResponse(click.getRoundId(), user.getNickname(), user.getColor(), victim.getNickname(), victim.getColor()));

            rankFacade.updateRankings(click, roomId);
            rankFacade.sendRank(roomId);

            //countDown(60);

            if (aliveCount > 1)
                return;

            roundFacade.endRound(roomId, roundId);
            System.out.println("라운드 종료!");

            if (round.getRoundNumber() == 3) {
                roundFacade.sendResult(roomId, roundId, null, round1Id, round2Id, round3Id);
                stop();
            }


            Round nextRound = roundFacade.startNextRound(round);
            roundFacade.sendResult(roomId, roundId, nextRound.getId(), round1Id, round2Id, round3Id);

            try{
                Thread.sleep(30000);
            }catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            roundFacade.initializeGameStatuses(roomId, round);
            updateNextRound(nextRound.getId());
        }
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

    private void saveSuccessfulClick(ClickDTO click) {
        String key = "roomId:" + roomId + ":availableClicks";
        redisUtil.saveToList(key, click);
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
        lastProcessedIndex = 0;
    }

}
