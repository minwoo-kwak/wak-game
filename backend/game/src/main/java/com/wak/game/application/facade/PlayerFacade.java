package com.wak.game.application.facade;

import com.wak.game.application.request.ClickRequest;
import com.wak.game.application.response.PlayerInfoResponse;
import com.wak.game.application.vo.clickVO;
import com.wak.game.domain.player.PlayerService;
import com.wak.game.domain.room.Room;
import com.wak.game.domain.room.RoomService;
import com.wak.game.domain.round.Round;
import com.wak.game.domain.round.RoundService;
import com.wak.game.domain.user.User;
import com.wak.game.domain.user.UserService;
import com.wak.game.global.error.ErrorInfo;
import com.wak.game.global.error.exception.BusinessException;
import com.wak.game.global.util.RedisUtil;
import com.wak.game.global.util.SocketUtil;
import com.wak.game.global.util.TimeUtil;
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
    private final RoomService roomService;

    private final TimeUtil timeUtil;

    public void saveClickLog(long roomId, ClickRequest request) {

        Room room = roomService.findById(roomId);

        Round round = roundService.findById(request.getRoundId());
        User user = userService.findById(request.getUserId());
        User victimUser = userService.findById(request.getVictimId());
        String currentTimeInNanos = timeUtil.getCurrentTimeInNanos();
        clickVO click = new clickVO(user.getId(), victimUser.getId(), round.getId(), request.getClickTime(), currentTimeInNanos);

        playerService.saveClickLog(room.getId(), click);
    }
}
