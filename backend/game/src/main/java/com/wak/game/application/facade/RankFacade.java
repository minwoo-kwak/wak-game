package com.wak.game.application.facade;

import com.wak.game.application.request.GameStartRequest;
import com.wak.game.application.response.DashBoardResponse;
import com.wak.game.application.response.GameStartResponse;
import com.wak.game.application.response.SummaryCountResponse;
import com.wak.game.application.response.socket.RankListResponse;
import com.wak.game.application.vo.clickVO;
import com.wak.game.domain.rank.dto.RankInfo;
import com.wak.game.domain.room.Room;
import com.wak.game.domain.room.RoomService;
import com.wak.game.domain.room.dto.RoomInfo;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class RankFacade {
    private final RedisUtil redisUtil;
    private final SocketUtil socketUtil;

    public void sendRank(Long roundId) {
        String key = "roundId" + roundId + ":ranks";
        Map<String, RankInfo> map = redisUtil.getData(key, RankInfo.class);

        List<RankInfo> ranks = new ArrayList<>(map.values());
        ranks.sort((r1, r2) -> Integer.compare(r2.getKillCnt(), r1.getKillCnt()));

        List<RankInfo> topRanks = ranks.stream()
                .limit(6)
                .collect(Collectors.toList());

        socketUtil.sendMessage("/topic/games/" + roundId + "/rank", new RankListResponse(topRanks));
    }

    public void updateRankings(clickVO click) {
        Long userId = click.userId();
        Long roundId = click.roundId();

        String key = "roundId:" + roundId + ":ranks";
        Map<String, Integer> curRoundRanks = redisUtil.getData(key, Integer.class);

        int curKillCnt = curRoundRanks.getOrDefault(Long.toString(userId), 0);
        curRoundRanks.put(Long.toString(userId), curKillCnt + 1);

        redisUtil.saveData(key, Long.toString(userId), curRoundRanks);
    }

}
