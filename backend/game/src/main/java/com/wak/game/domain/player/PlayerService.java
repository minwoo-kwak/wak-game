package com.wak.game.domain.player;

import com.wak.game.application.response.PlayerInfoResponse;
import com.wak.game.application.vo.clickVO;
import com.wak.game.domain.player.dto.PlayerInfo;
import com.wak.game.domain.round.Round;
import com.wak.game.global.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PlayerService {
    private final RedisUtil redisUtil;

    public List<PlayerInfoResponse> getPlayersInfo(Round round) {
        String key = "roundId:" + round.getId()+":users";
        Map<String, PlayerInfo> playersMap = redisUtil.getData(key, PlayerInfo.class);

        List<PlayerInfoResponse> players = new ArrayList<>();

        for (Map.Entry<String, PlayerInfo> entry : playersMap.entrySet()) {
            PlayerInfo player = entry.getValue();
            players.add(
                    PlayerInfoResponse.builder()
                            .roundId(round.getId())
                            .userId(player.getUserId())
                            .nickname(player.getNicKName())
                            .color(player.getColor())
                            .stamina(player.getStamina())
                            .team(player.getTeam())
                            .build()
            );
        }

        return players;
    }

    public void saveClickLog(Round round, clickVO click) {
        String key = "round:" + round.getId() + ":clicks";
        double score = System.currentTimeMillis(); // 시간을 점수로 사용

        redisUtil.saveData(key, String.valueOf(click), score);
    }
}


