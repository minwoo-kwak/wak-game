package com.wak.game.domain.round.thread;

import com.wak.game.application.facade.RankFacade;
import com.wak.game.application.facade.RoundFacade;
import com.wak.game.application.response.socket.KillLogResponse;
import com.wak.game.domain.player.dto.PlayerInfo;
import com.wak.game.domain.round.Round;
import com.wak.game.domain.round.RoundService;
import com.wak.game.domain.round.dto.ClickDTO;
import com.wak.game.global.error.ErrorInfo;
import com.wak.game.global.error.exception.BusinessException;
import com.wak.game.global.util.RedisUtil;
import com.wak.game.global.util.SocketUtil;
import jakarta.transaction.Transactional;

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
    private final SocketUtil socketUtil;
    private final RoundService roundService;
    private final RoundFacade roundFacade;
    private final RankFacade rankFacade;

    public ClickEventProcessor(Long roundId, Long roomId, int playerCnt, RedisUtil redisUtil, SocketUtil socketUtil, RoundService roundService, RoundFacade roundFacade, RankFacade rankFacade) {
        this.roundId = roundId;
        this.round1Id = roundId;
        this.roomId = roomId;
        this.playerCount = playerCnt;
        this.redisUtil = redisUtil;
        this.socketUtil = socketUtil;
        this.roundService = roundService;
        this.roundFacade = roundFacade;
        this.rankFacade = rankFacade;
        this.aliveCount = playerCount;
    }

    @Override
    public void run() {
        //countDown(3);

        while (running) {
            try {
                List<ClickDTO> clickDataList = redisUtil.getListData("roomId:" + roomId + ":clicks", ClickDTO.class, lastProcessedIndex);

                if (clickDataList.isEmpty()) {
                    continue;
                }

                for (int i = 0; i < clickDataList.size(); i++) {
                    ClickDTO click = clickDataList.get(i);
                    if (click == null)
                        continue;

                    lastProcessedIndex++;
                    checkClickedUser(click);
                    if (!running)
                        break;
                }

                Thread.sleep(1); // 10밀리초 대기
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Transactional
    protected void checkClickedUser(ClickDTO click) {
        Round round = roundService.findById(click.getRoundId());

        if (!click.getRoundId().equals(this.roundId))
            throw new BusinessException(ErrorInfo.THREAD_ID_IS_DIFFERENT);

        if (!round.getId().equals(roundId))
            throw new BusinessException(ErrorInfo.ROUND_NOT_MATCHED);

        String key = "roomId:" + roomId + ":users";
        Map<String, PlayerInfo> data = redisUtil.getData(key, PlayerInfo.class);

        PlayerInfo user = data.get(click.getUserId().toString());
        PlayerInfo victim = data.get(click.getVictimId().toString());
        if (user == null) {
            throw new BusinessException(ErrorInfo.PLAYER_NOT_FOUND);
        }
        if (victim == null) {
            throw new BusinessException(ErrorInfo.PLAYER_NOT_FOUND);
        }


        if (isAlive(user) && isAlive(victim)) {
            victim.updateStamina(-1);
            redisUtil.saveData(key, victim.getUserId().toString(), victim);

            saveSuccessfulClick(click);

            roundFacade.sendBattleField(roomId, false);
            roundFacade.sendDashBoard(roomId, round.getRoundNumber());

            --aliveCount;

            saveSuccessfulClick(click);
            socketUtil.sendMessage("/games/" + roomId + "/kill-log", new KillLogResponse(click.getRoundId(), user.getNickname(), user.getColor(), victim.getNickname(), victim.getColor()));

            rankFacade.updateRankings(click, roomId);
            rankFacade.sendRank(roomId);

            //countDown(60);

            if (aliveCount > 1)
                return;

            roundFacade.endRound(roomId, roundId);

            if (round.getRoundNumber() == 3) {
                roundFacade.sendResult(roomId, roundId, null, round1Id, round2Id, round3Id);
                //todo 룸상태를대기실로 바꿔야 함.

                //roundFacade.endGame(roomId);
                stop();
            }

            Round nextRound = roundFacade.startNextRound(round);
            roundFacade.sendResult(roomId, roundId, nextRound.getId(), round1Id, round2Id, round3Id);

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            roundFacade.initializeGameStatuses(roomId, nextRound);
            roundFacade.sendDashBoard(roomId, nextRound.getRoundNumber());
            rankFacade.sendRank(roomId);
            updateNextRound(nextRound.getId());
        }
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
