package com.wak.game.domain.round;

import com.wak.game.application.request.GameStartRequest;
import com.wak.game.application.response.SummaryCountResponse;
import com.wak.game.application.vo.RoomVO;
import com.wak.game.domain.player.dto.PlayerInfo;
import com.wak.game.domain.rank.dto.RankInfo;
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

        for (Map.Entry<String, RoomVO> entry : map.entrySet()) {
            RoomVO roomUser = entry.getValue();
            PlayerInfo gameUser = new PlayerInfo(roomUser.userId(), roomUser.color(), roomUser.nickname(), roomUser.team(), roomUser.isHost(), 1);
            RankInfo rankInfo = RankInfo.builder()
                    .killCnt(0)
                    .nickName(roomUser.nickname())
                    .userId(roomUser.userId())
                    .build();
            String userkey = "roundId:" + round.getId() + ":users";
            String rankKey = "roundId:" + round.getId() + ":ranks";

            redisUtil.saveData(userkey, "userId:" + roomUser.userId(), gameUser);
            redisUtil.saveData(rankKey, "userId:" + roomUser.userId(), rankInfo);

            playersId.add(gameUser.getUserId());
        }

        // todo: roundId:___:ranks키로 HashMap<userId, RankInfo(킬수, 닉네임, userId)> 넣어놓기

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
                if (player.getStamina() > 0)
                    return true;

                return false;
            }
        }

        throw new BusinessException(ErrorInfo.USER_NOT_EXIST);
    }

    /**
     * 클릭 유효성 처리하는 스레드 생성 및 실행
     *
     * @param id
     */
    public void startThread(Long id) {
        //todo 생성자 다시 생각해보기
        ClickEventProcessor clickProcessor = applicationContext.getBean(ClickEventProcessor.class, id);
        Thread thread = new Thread(clickProcessor);

        thread.start();

        gameThreads.put(id, thread);
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

}


