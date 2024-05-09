package com.wak.game.domain.round;

import com.wak.game.application.request.GameStartRequest;
import com.wak.game.application.response.SummaryCountResponse;
import com.wak.game.application.vo.RoomVO;
import com.wak.game.application.vo.gameVO;
import com.wak.game.domain.room.Room;
import com.wak.game.domain.user.User;
import com.wak.game.global.error.ErrorInfo;
import com.wak.game.global.error.exception.BusinessException;
import com.wak.game.global.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class RoundService {
    private final RoundRepository roundRepository;
    private final RedisUtil redisUtil;

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
        Map<String, RoomVO> map = redisUtil.getData(String.valueOf(room.getId()), RoomVO.class);
        List<Long> playersId = new ArrayList<>();

        for (Map.Entry<String, RoomVO> entry : map.entrySet()) {
            RoomVO roomUser = entry.getValue();
            gameVO gameUser = new gameVO(roomUser.userId(), roomUser.color(), roomUser.nickname(), roomUser.team(), roomUser.isHost(), 1);

            String key = "roundId:"+round.getId()+":users";
            redisUtil.saveData(key, "userId:" + roomUser.userId(), gameUser);

            playersId.add(gameUser.userId());
        }

        return playersId;
    }

    public SummaryCountResponse getSummaryCount(Round round) {
        String key = "roundId:" + round.getId()+":users";
        Map<String, gameVO> result = redisUtil.getData(key, gameVO.class);

        int aliveCount = 0;
        for (Map.Entry<String, gameVO> entry : result.entrySet()) {
            gameVO player = entry.getValue();
            if (player.stamina() > 0)
                aliveCount++;

        }
        return SummaryCountResponse.builder()
                .aliveCount(aliveCount)
                .totalCount(result.size())
                .build();
    }

    public boolean isAlive(Round round, User user) {
        String key = "roundId:" + round.getId()+":users";
        Map<String, gameVO> result = redisUtil.getData(key, gameVO.class);
        for (Map.Entry<String, gameVO> entry : result.entrySet()) {
            gameVO player = entry.getValue();
            if (player.userId() == user.getId()){
                if(player.stamina()>0)
                    return true;
                return false;
            }
        }
        throw new BusinessException(ErrorInfo.USER_NOT_EXIST);
    }


   /* public boolean isAlive(Round round, User user) {

        return false;
    }*/
}
