package com.wak.game.domain.player;

import com.wak.game.application.response.PlayerInfoResponse;
import com.wak.game.application.vo.clickVO;
import com.wak.game.application.vo.gameVO;
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
        //레디스에서 roundId:를 키로 저장된
        String key = "roundId:" + round.getId()+":users";
        Map<String, gameVO> playersMap = redisUtil.getData(key, gameVO.class);
        List<PlayerInfoResponse> players = new ArrayList<>();
        for (Map.Entry<String, gameVO> entry : playersMap.entrySet()) {
            gameVO player = entry.getValue();
            players.add(
                    PlayerInfoResponse.builder()
                            .roundId(round.getId())
                            .userId(player.userId())
                            .nickname(player.nickname())
                            .hexColor(player.hexColor())
                            .stamina(player.stamina())
                            .team(player.team())
                            .build()
            );
        }

        return players;
    }

    public void saveClickLog(Round round, clickVO click) {


        //<roundId, List<clickVO>>
        String key = "round:" + round.getId() + ":clicks";
        double score = System.currentTimeMillis(); // 시간을 점수로 사용
        redisUtil.saveData(key, String.valueOf(click), score);
    }
}


