package com.wak.game.domain.round;

import com.wak.game.application.request.GameStartRequest;
import com.wak.game.application.response.SummaryCountResponse;
import com.wak.game.application.vo.RoomVO;
import com.wak.game.domain.player.dto.PlayerInfo;
import com.wak.game.domain.rank.dto.RankInfo;
import com.wak.game.domain.round.dto.PlayerCount;
import com.wak.game.domain.round.thread.ClickEventProcessor;
import com.wak.game.domain.room.Room;
import com.wak.game.domain.user.User;
import com.wak.game.global.error.ErrorInfo;
import com.wak.game.global.error.exception.BusinessException;
import com.wak.game.global.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

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
        return roundRepository.findById(roundId).orElseThrow(() -> new BusinessException(ErrorInfo.ROOM_NOT_EXIST));
    }

    public Round startRound(Room room, GameStartRequest gameStartRequest) {
        Round round = Round.builder()
                .roundNumber(gameStartRequest.getRoundNumber())
                .room(room)
                .aggro(gameStartRequest.getComment())
                .showNickname(gameStartRequest.isShowNickname())
                .build();

        return roundRepository.save(round);
    }

    public Round startRound(Round round, String aggro) {
        return roundRepository.save(Round.builder()
                .roundNumber(round.getRoundNumber() + 1)
                .room(round.getRoom())
                .aggro(aggro)
                .showNickname(round.getShowNickname())
                .build());
    }

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

    /**
     * 대기방(레디스) 유저 -> 게임중(레디스) 유저로 덮어씌우기
     *
     * @param room
     * @param round
     * @return
     */
    public List<Long> initializeGameStatuses(Room room, Round round) {
        Map<String, RoomVO> map = redisUtil.getRoomUsersInfo(room.getId());
        List<Long> playersId = new ArrayList<>();

        int teamATotal = 0;
        int teamBTotal = 0;

        for (Map.Entry<String, RoomVO> entry : map.entrySet()) {
            RoomVO roomUser = entry.getValue();
            //todo:  RoomVO에 관전자인지, 여왕인지 여부가 없음.
            PlayerInfo gameUser = new PlayerInfo(roomUser.userId(), roomUser.color(), roomUser.nickname(), roomUser.team(), roomUser.isHost(), 1);
            RankInfo rankInfo = RankInfo.builder()
                    .killCnt(0)
                    .nickName(roomUser.nickname())
                    .userId(roomUser.userId())
                    .build();

            String userKey = "roundId:" + round.getId() + ":users";
            String rankKey = "roundId:" + round.getId() + ":ranks";

            redisUtil.saveData(userKey, Long.toString(roomUser.userId()), gameUser);
            redisUtil.saveData(rankKey, Long.toString(roomUser.userId()), rankInfo);

            playersId.add(gameUser.getUserId());

            if (roomUser.team().equals("A"))
                teamATotal++;
            else if (roomUser.team().equals("B"))
                teamBTotal++;
        }

        String key = "aliveAndTotalPlayers";
        PlayerCount count = PlayerCount.builder()
                .aliveCountA(teamATotal)
                .totalCountA(teamATotal)
                .aliveCountB(teamBTotal)
                .totalCountB(teamBTotal)
                .build();

        redisUtil.saveData(key, round.getId().toString(), count);

        return playersId;
    }

    public SummaryCountResponse getSummaryCount(Round round) {
        String key = "roundId:" + round.getId() + ":users";
        Map<String, PlayerInfo> result = redisUtil.getData(key, PlayerInfo.class);
        int aliveCount = 0;

        for (Map.Entry<String, PlayerInfo> entry : result.entrySet()) {
            PlayerInfo player = entry.getValue();

            if (player.getStamina() > 0)
                aliveCount++;

        }

        return SummaryCountResponse.builder()
                .aliveCount(aliveCount)
                .totalCount(result.size())
                .build();
    }

    public boolean isAlive(Round round, User user) {
        String key = "roundId:" + round.getId() + ":users";
        Map<String, PlayerInfo> result = redisUtil.getData(key, PlayerInfo.class);

        for (Map.Entry<String, PlayerInfo> entry : result.entrySet()) {
            PlayerInfo player = entry.getValue();

            if (player.getUserId() == user.getId()) {
                return player.getStamina() > 0;
            }
        }

        throw new BusinessException(ErrorInfo.USER_NOT_EXIST);
    }

    public void startThread(Long roomId, Long roundId) {
        ClickEventProcessor clickProcessor = applicationContext.getBean(ClickEventProcessor.class, roundId, roomId);
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
            System.out.println("Game stopped in room: " + id);
        }
    }

    public void deleteRound(Long id) {
        roundRepository.deleteRound(id);
    }

}


