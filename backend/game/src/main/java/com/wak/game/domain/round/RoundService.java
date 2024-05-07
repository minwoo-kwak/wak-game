package com.wak.game.domain.round;

import com.wak.game.application.controller.dto.AlivePlayerResponse;
import com.wak.game.application.controller.dto.GameStartRequest;
import com.wak.game.domain.room.Room;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class RoundService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final RoundRepository roundRepository;

    public Round startRound(Room room, GameStartRequest gameStartRequest) {

        Round round = Round.builder()
                .roundNumber(gameStartRequest.getRoundNumber())
                .room(room)
                .aggro(gameStartRequest.getComment())
                .showNickname(gameStartRequest.isShowNickname())
                .build();

        return roundRepository.save(round);
    }

    public List<AlivePlayerResponse> initializeGameStatuses(Round currentRound, Long roomId) {
        List<AlivePlayerResponse> statuses = new ArrayList<>();
        Set<String> userKeys = redisTemplate.keys("room:" + roomId + ":user:*");
        for (String userKey : userKeys) {
            Map<Object, Object> userData = redisTemplate.opsForHash().entries(userKey);
            AlivePlayerResponse status = new AlivePlayerResponse(
                    currentRound.getId(),
                    Long.parseLong(userData.get("userId").toString()),
                    1,//일단  개인전
                    userData.get("team").toString(),
                    userData.get("hexColor").toString()
            );
            statuses.add(status);
        }
        return statuses;
    }

    public void updateGameStatus(Long roundId, List<AlivePlayerResponse> players) {
        players.forEach(player -> {
            String key = "gameStatus:" + roundId + ":user:" + player.getUserId();
            Map<String, String> map = new HashMap<>();
            map.put("stamina", String.valueOf(player.getStamina()));
            map.put("team", player.getTeam());
            map.put("hexColor", player.getHexColor());
            redisTemplate.opsForHash().putAll(key, map);
        });
    }
}
