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
import com.wak.game.global.util.RedisUtil;
import com.wak.game.global.util.SocketUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Formatter;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class PlayerFacade {
    private final PlayerService playerService;
    private final RoundService roundService;
    private final UserService userService;
    private final RedisUtil redisUtil;
    private final SocketUtil socketUtil;

    public void getPlayersStatus(Long roundId) {
        Round round = roundService.findById(roundId);
        List<PlayerInfoResponse> playersInfo = playerService.getPlayersInfo(round);
        socketUtil.sendMessage("/topic/games/" + roundId + "/battle-field", playersInfo);
    }

    public void saveClickLog(ClickRequest request) {
        Round round = roundService.findById(request.getRoundId());
        User user = userService.findById(request.getUserId());
        User victimUser = userService.findById(request.getVictimId());
        clickVO click = new clickVO(user.getId(), victimUser.getId(), round.getId(), request.getClickTime());

        System.out.println("click: "+click.userId().toString());
        playerService.saveClickLog(round, click);
    }

    public void getKillLog(Long roundId) {
        Round round = roundService.findById(roundId);

        String key = "roundId:" + roundId + ":availableClicks";

        Map<String, clickVO> data = redisUtil.getData(key, clickVO.class);

    }

    public void convertToPlayer(Long roundId, Long userId) {

    }

   /* public List<AlivePlayerResponse> getAliveUsers(Long roomRoundId) {
        //레디스 뒤져서 stamina가 1 이상인 사람들 찾아오기
        return playerService.getAliveUsers(roomRoundId);
    }*/

}
