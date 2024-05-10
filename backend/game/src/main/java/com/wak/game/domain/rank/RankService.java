package com.wak.game.domain.rank;

import com.wak.game.application.vo.clickVO;
import com.wak.game.global.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class RankService {
    private RedisUtil redisUtil;

    public void updateRankings(clickVO click) {
        Long userId = click.userId();
        Long roundId = click.roundId();

        String key = "roundId:" + roundId + ":rankings";
        Map<String, Integer> curRoundRanks = redisUtil.getData(key, Integer.class);

        int curKillCnt = curRoundRanks.getOrDefault(Long.toString(userId), 0);
        curRoundRanks.put(Long.toString(userId), curKillCnt + 1);

        redisUtil.saveData(key, Long.toString(userId), curRoundRanks);
    }
}
