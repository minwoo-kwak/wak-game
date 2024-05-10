package com.wak.game.application.facade;

import com.wak.game.application.request.GameStartRequest;
import com.wak.game.application.response.DashBoardResponse;
import com.wak.game.application.response.GameStartResponse;
import com.wak.game.application.response.SummaryCountResponse;
import com.wak.game.application.vo.RoomInfoVO;
import com.wak.game.domain.room.Room;
import com.wak.game.domain.room.RoomService;
import com.wak.game.domain.round.Round;
import com.wak.game.domain.round.RoundService;
import com.wak.game.domain.user.User;
import com.wak.game.domain.user.UserService;
import com.wak.game.global.util.RedisUtil;
import com.wak.game.global.util.SocketUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class RoundFacade {

    private final RoundService roundService;
    private final RoomService roomService;
    private final UserService userService;
    private final RedisUtil redisUtil;
    private final SocketUtil socketUtil;

    public GameStartResponse startGame(GameStartRequest gameStartRequest, Long roomId, Long userId) {
        User user = userService.findById(userId);
        Room room = roomService.findById(roomId);
        roomService.isHost(user, room);
        roomService.isInGame(room);

        Round round = roundService.startRound(room, gameStartRequest);

        List<Long> players = roundService.initializeGameStatuses(room,round);

        //FE에서 웹소켓 구독하기 위해 현재 게임에 참여한 userId 리스트로 담아서 반환(List<Long>)
        return GameStartResponse.of(players);
    }

    public DashBoardResponse getDashBoard(Long roundId, Long userId) {

        Round round = roundService.findById(roundId);
        User user = userService.findById(userId);

       //레디스에서
        SummaryCountResponse summary = roundService.getSummaryCount(round);
        //해당 user가 살았는지
        boolean isAlive = roundService.isAlive(round,user);

        return DashBoardResponse.builder()
                .roomName(round.getRoom().getRoomName())
                .totalCount(summary.getTotalCount())
                .aliveCount(summary.getAliveCount())
                .isAlive(isAlive)
                .build();

    }


    public void gameStart(Room room) {
        RoomInfoVO roomInfoVO = redisUtil.getLobbyRoomInfo(room.getId());

        roomInfoVO.gameStart();
        redisUtil.saveData("roomInfo", String.valueOf(room.getId()), roomInfoVO);
        roomService.gameStart(room);
        socketUtil.sendRoomList();
    }

    public void gameEnd(Room room) {

    }
}
