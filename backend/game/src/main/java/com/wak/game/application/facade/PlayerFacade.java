package com.wak.game.application.facade;

import com.wak.game.domain.player.PlayerService;
import com.wak.game.domain.player.dto.AlivePlayerResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class PlayerFacade {
    private final RedisTemplate<String, String> redisTemplate;
    private final PlayerService playerService;


   /* public List<AlivePlayerResponse> getAliveUsers(Long roomRoundId) {
        //레디스 뒤져서 stamina가 1 이상인 사람들 찾아오기
        return playerService.getAliveUsers(roomRoundId);
    }
*/

}
