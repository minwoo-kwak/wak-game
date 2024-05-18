package com.wak.game.domain.playerLog;

import com.wak.game.application.vo.clickVO;
import com.wak.game.domain.player.Player;
import com.wak.game.global.error.ErrorInfo;
import com.wak.game.global.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PlayerLogService {
    private final PlayerLogRepository playerLogRepository;
    public void saveAll(List<PlayerLog> logs) {
        playerLogRepository.saveAll(logs);
    }

    public PlayerLog createPlayerLog(clickVO click, Map<Long, Player> playerMap) {
        Player player = playerMap.get(click.userId());

        if (player == null) {
            throw new BusinessException(ErrorInfo.PLAYER_NOT_FOUND);
        }

        String logDetail = click.toString();
        return new PlayerLog(player, logDetail);
    }
}
