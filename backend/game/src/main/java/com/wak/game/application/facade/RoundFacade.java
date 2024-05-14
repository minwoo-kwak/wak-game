package com.wak.game.application.facade;

import com.wak.game.application.request.GameStartRequest;
import com.wak.game.application.response.DashBoardResponse;
import com.wak.game.application.response.GameStartResponse;
import com.wak.game.application.response.SummaryCountResponse;
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

        if (gameStartRequest.getRoundNumber() != 1) {
            throw new BusinessException(ErrorInfo.ROUND_NOT_ONE);
        }

        roomService.isHost(user, room);
        roomService.isInGame(room);

        RoomInfo roomInfo = redisUtil.getLobbyRoomInfo(room.getId());

        roomInfo.gameStart();
        redisUtil.saveData("roomInfo", String.valueOf(room.getId()), roomInfo);

        roomService.gameStart(room);
//        socketUtil.sendRoomList();
        socketUtil.sendMessage("/rooms", room.getId().toString(), "GAME START");


        return GameStartResponse.of(startRound(gameStartRequest, room));
    }

    //TODO: 1라운드 시작
    public List<Long> startRound(GameStartRequest gameStartRequest, Room room) {
        Round round = roundService.startRound(room, gameStartRequest);

        // todo: 현재 방이 몇라운드인지 확인하기 위해 레디스에 저장(생존자/참가자 비율이 1/2 일때 라운드를 종료하기 위함)
        String key = "roomId:roundNumbers";
        redisUtil.saveData(key, room.getId().toString(), String.valueOf(round.getRoundNumber()));

        List<Long> players = roundService.initializeGameStatuses(room, round);
        roundService.startThread(room.getId());
        return players;
    }

    //TODO: 2라운드 3라운드 시작
    public List<Long> startRound(Round r, Room room) {
        Round round = roundService.startRound(r, " ");
        List<Long> players = roundService.initializeGameStatuses(room, round);

        String key = "roomId:roundNumbers";
        redisUtil.saveData(key, room.getId().toString(), String.valueOf(round.getRoundNumber()));

        roundService.startThread(room.getId());

        return players;
    }


    public void endRound(Long roundId) {
        Round round = roundService.findById(roundId);
        Room room = roomService.findById(round.getRoom().getId());

        roomService.isNotInGame(room);

        roundService.deleteRound(roundId);
        socketUtil.sendMessage("/rooms", room.getId().toString(), "ROUND END");

        // 게임 시작 시 만들어 놨던 redis 저장소를 다 없앤다.
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


}
