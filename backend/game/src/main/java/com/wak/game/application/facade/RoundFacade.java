package com.wak.game.application.facade;

import com.wak.game.application.request.GameStartRequest;
import com.wak.game.application.response.DashBoardResponse;
import com.wak.game.application.response.GameStartResponse;
import com.wak.game.application.response.SummaryCountResponse;
import com.wak.game.application.response.socket.RankListResponse;
import com.wak.game.domain.room.dto.RoomInfo;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RequiredArgsConstructor
@Service
public class RoundFacade {

    private final RoundService roundService;
    private final RoomService roomService;
    private final UserService userService;
    private final RedisUtil redisUtil;
    private final SocketUtil socketUtil;

    @Transactional
    public GameStartResponse startGame(GameStartRequest gameStartRequest, Long roomId, Long userId) {
        User user = userService.findById(userId);
        Room room = roomService.findById(roomId);

        roomService.isHost(user, room);
        roomService.isInGame(room);

        RoomInfo roomInfo = redisUtil.getLobbyRoomInfo(room.getId());
        roomInfo.gameStart();
        redisUtil.saveData("roomInfo", String.valueOf(room.getId()), roomInfo);

        roomService.gameStart(room);
        socketUtil.sendMessage("/rooms", room.getId().toString(), "GAME START");

        return startRound(gameStartRequest, room);
    }

    public GameStartResponse startRound(GameStartRequest gameStartRequest, Room room) {
        Round round = roundService.startRound(room, gameStartRequest);
        roundService.initializeGameStatuses(room, round);

        roundService.startThread(room.getId(), round.getId());
        return GameStartResponse.of(round.getId());
    }

    public Round startNextRound(Round previousRound) {
        return roundService.startNextRound(previousRound);
    }

    public void endRound(Long roundId) {
        Round round = roundService.findById(roundId);
        Room room = roomService.findById(round.getRoom().getId());

        roomService.isNotInGame(room);

        roundService.deleteRound(roundId);
        socketUtil.sendMessage("/rooms", room.getId().toString(), "ROUND END");

        // 게임 시작 시 만들어 놨던 redis 저장소를 다 없앤다.
        // todo: availableClicks에 있는 정보에 기반한 players 로그들 다 저장한다.
        // todo: 모든 클릭 로그들을 player_logs에 저장한다.
        // todo : 관전자인지, 무슨팀인지, 여왕여부 이런거 없음.
        redisUtil.deleteKey("roundId:" + roundId + ":users");

        // gameEnd 할지 결정
        if ((room.getMode().toString().equals("SOLO") && round.getRoundNumber() == 3)
                || room.getMode().toString().equals("TEAM")) {
            endGame(room);
        }

    }

    public void endGame(Room room) {
        RoomInfo roomInfo = redisUtil.getLobbyRoomInfo(room.getId());

        roomInfo.gameEnd();
        roomService.gameEnd(room);
        redisUtil.saveData("roomInfo", String.valueOf(room.getId()), roomInfo);
        socketUtil.sendRoomList();
        socketUtil.sendMessage("/rooms", room.getId().toString(), "ROUND END");
        socketUtil.sendMessage("/rooms", room.getId().toString(), "GAME END");
    }

    public DashBoardResponse getDashBoard(Long roundId) {
        Round round = roundService.findById(roundId);
        SummaryCountResponse summary = roundService.getSummaryCount(round);

        return DashBoardResponse.builder()
                .totalCount(summary.getTotalCount())
                .aliveCount(summary.getAliveCount())
                .roundId(roundId)
                .build();
    }

    public void sendDashBoard(Long roundId) {
        Round round = roundService.findById(roundId);
        SummaryCountResponse summaryCount = roundService.getSummaryCount(round);

        socketUtil.sendMessage("topic/games/" + round.getId() + "/dashboard", round.getId().toString(), summaryCount);
    }
}
