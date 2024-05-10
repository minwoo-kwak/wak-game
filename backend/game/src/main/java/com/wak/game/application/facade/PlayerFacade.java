package com.wak.game.application.facade;

import com.wak.game.application.request.ClickRequest;
import com.wak.game.application.response.PlayerInfoResponse;
import com.wak.game.application.vo.clickVO;
import com.wak.game.domain.player.PlayerService;
import com.wak.game.domain.round.Round;
import com.wak.game.domain.round.RoundService;
import com.wak.game.domain.user.User;
import com.wak.game.domain.user.UserService;
import com.wak.game.global.error.ErrorInfo;
import com.wak.game.global.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Formatter;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class PlayerFacade {
    private final RedisTemplate<String, String> redisTemplate;
    private final PlayerService playerService;
    private final RoundService roundService;
    private final UserService userService;

    public List<PlayerInfoResponse> getPlayersStatus(Long roundId) {
        Round round = roundService.findById(roundId);
        return playerService.getPlayersInfo(round);
    }

    public void saveClickLog(Long userId, Long roundId, ClickRequest request) {
        Round round = roundService.findById(roundId);
        User user = userService.findById(userId);
        User victimUser = userService.findById(request.getVictimId());
        clickVO click = new clickVO(user.getId(), victimUser.getId(), round.getId(), request.getClickTime());
        playerService.saveClickLog(round, click);
    }

   /* public List<AlivePlayerResponse> getAliveUsers(Long roomRoundId) {
        //레디스 뒤져서 stamina가 1 이상인 사람들 찾아오기
        return playerService.getAliveUsers(roomRoundId);
    }*/

}
