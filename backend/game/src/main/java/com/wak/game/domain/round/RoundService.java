package com.wak.game.domain.round;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wak.game.application.facade.RankFacade;
import com.wak.game.application.facade.RoundFacade;
import com.wak.game.application.request.GameStartRequest;
import com.wak.game.application.response.SummaryCountResponse;
import com.wak.game.domain.player.PlayerService;
import com.wak.game.domain.player.dto.PlayerInfo;
import com.wak.game.domain.round.thread.ClickEventProcessor;
import com.wak.game.domain.room.Room;
import com.wak.game.domain.user.UserService;
import com.wak.game.global.error.ErrorInfo;
import com.wak.game.global.error.exception.BusinessException;
import com.wak.game.global.util.RedisUtil;
import com.wak.game.global.util.SocketUtil;
import com.wak.game.global.util.TimeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class RoundService {

    private final RoundRepository roundRepository;
    private final RedisUtil redisUtil;
    private final ApplicationContext applicationContext;
    private final ConcurrentHashMap<Long, Thread> gameThreads = new ConcurrentHashMap<>();

    public Round findById(Long roundId) {
        return roundRepository.findById(roundId).orElseThrow(() -> new BusinessException(ErrorInfo.ROUND_NOT_EXIST));
    }

    @Transactional
    public Round startRound(Room room, GameStartRequest gameStartRequest) {
        Round round = Round.builder()
                .roundNumber(1)
                .room(room)
                .aggro(gameStartRequest.getComment())
                .showNickname(gameStartRequest.isShowNickname())
                .build();

        return roundRepository.save(round);
    }

    @Transactional
    public Round startNextRound(Round previousRound) {
        Room room = previousRound.getRoom();
        Round nextRound = Round.builder()
                .roundNumber(previousRound.getRoundNumber() + 1)
                .room(room)
                .aggro(previousRound.getAggro())
                .showNickname(previousRound.getShowNickname())
                .build();

        roundRepository.save(nextRound);
        return nextRound;
    }


    public SummaryCountResponse getSummaryCount(Long roomId, int roundNumber) {
        String key = "roomId:" + roomId + ":users";
        Map<String, PlayerInfo> result = redisUtil.getData(key, PlayerInfo.class);
        int aliveCount = 0;

        for (Map.Entry<String, PlayerInfo> entry : result.entrySet()) {
            PlayerInfo player = entry.getValue();

            if (player.getStamina() > 0)
                aliveCount++;

        }

        return SummaryCountResponse.builder()
                .roundNumber(roundNumber)
                .aliveCount(aliveCount)
                .totalCount(result.size())
                .build();
    }

    public void startThread(Long roomId, Long roundId, int playerCnt) {
        System.out.println("스레드 실행 함수 ");

        RedisUtil redisUtil = applicationContext.getBean(RedisUtil.class);
        ObjectMapper objectMapper = applicationContext.getBean(ObjectMapper.class);
        SocketUtil socketUtil = applicationContext.getBean(SocketUtil.class);
        RoundService roundService = applicationContext.getBean(RoundService.class);
        PlayerService playerService = applicationContext.getBean(PlayerService.class);
        RoundFacade roundFacade = applicationContext.getBean(RoundFacade.class);
        RankFacade rankFacade = applicationContext.getBean(RankFacade.class);
        UserService userService = applicationContext.getBean(UserService.class);
        TimeUtil timeUtil = applicationContext.getBean(TimeUtil.class);

        ClickEventProcessor clickProcessor = new ClickEventProcessor(roundId, roomId, playerCnt, redisUtil, objectMapper, socketUtil, roundService, playerService, roundFacade, rankFacade, userService, timeUtil);
        Thread thread = new Thread(clickProcessor);
        thread.start();

        gameThreads.put(roomId, thread);
    }

    /**
     * 클릭 유효성 처리하는 스레드 종료 및 제거
     * + round가 끝날때마다 스레드 제거하고 재생성 VS round3까지 유지
     *
     * @param id
     */
    public void endThread(Long id) {
        Thread thread = gameThreads.remove(id);

        if (thread != null) {
            thread.interrupt();
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    @Transactional
    public void deleteRound(Long id) {
        roundRepository.deleteRound(id);
    }

    public void save(Round round) {
        roundRepository.save(round);
    }
}


