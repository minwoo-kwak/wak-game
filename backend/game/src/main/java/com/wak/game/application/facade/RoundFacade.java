package com.wak.game.application.facade;

import com.wak.game.application.controller.dto.AlivePlayerResponse;
import com.wak.game.application.controller.dto.GameStartRequest;
import com.wak.game.domain.room.Room;
import com.wak.game.domain.room.RoomService;
import com.wak.game.domain.round.Round;
import com.wak.game.domain.round.RoundService;
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

    public List<AlivePlayerResponse> startGameAndReturnStatus(GameStartRequest gameStartRequest, Long roomId) {
        Room room = roomService.findById(roomId);
        Round round = roundService.startRound(room, gameStartRequest);
        List<AlivePlayerResponse> aliveUsers = roundService.initializeGameStatuses(round, roomId);
        roundService.updateGameStatus(round.getId(), aliveUsers);
        return aliveUsers;
    }

}
