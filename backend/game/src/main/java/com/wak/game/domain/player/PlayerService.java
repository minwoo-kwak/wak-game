package com.wak.game.domain.player;

import com.wak.game.application.response.PlayerInfoResponse;
import com.wak.game.application.vo.clickVO;
import com.wak.game.domain.player.dto.PlayerInfo;
import com.wak.game.domain.round.Round;
import com.wak.game.global.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PlayerService {
    private final RedisUtil redisUtil;
    private final PlayerRepository playerRepository;

    public List<PlayerInfoResponse> getPlayersInfo(Round round) {
        String key = "roundId:" + round.getId() + ":users";
        Map<String, PlayerInfo> playersMap = redisUtil.getData(key, PlayerInfo.class);
        return buildPlayerInfoList(round, playersMap);
    }

    private List<PlayerInfoResponse> buildPlayerInfoList(Round round, Map<String, PlayerInfo> playersMap) {
        List<PlayerInfoResponse> players = new ArrayList<>();
        boolean showNickname = round.getShowNickname();

        for (Map.Entry<String, PlayerInfo> entry : playersMap.entrySet()) {
            players.add(buildPlayerInfoResponse(round, entry.getValue(), showNickname));
        }

        return players;
    }

    private PlayerInfoResponse buildPlayerInfoResponse(Round round, PlayerInfo player, boolean showNickname) {
        PlayerInfoResponse.PlayerInfoResponseBuilder responseBuilder = PlayerInfoResponse.builder()
                .roundId(round.getId())
                .userId(player.getUserId())
                .color(player.getColor())
                .stamina(player.getStamina())
                .team(player.getTeam());

        if (showNickname) {
            responseBuilder.nickname(player.getNicKName());
        } else {
            responseBuilder.nickname("");
        }

        return responseBuilder.build();
    }

    public void saveClickLog(Round round, clickVO click) {
        String key = "round:" + round.getId() + ":clicks";
        double score = System.currentTimeMillis(); // 시간을 점수로 사용

        redisUtil.saveData(key, String.valueOf(click), score);
    }

    @Transactional
    public void savePlayers(List<Player> players) {
        playerRepository.saveAll(players);
    }
}


