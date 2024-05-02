package com.wak.game.application.facade;

import com.wak.game.application.request.RoomCreateRequest;
import com.wak.game.application.request.RoomEnterRequest;
import com.wak.game.application.response.RoomCreateResponse;
import com.wak.game.domain.room.RoomService;
import com.wak.game.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class RoomFacade {
    private final RoomService roomService;
    private final UserService userService;


    public RoomCreateResponse createRoom(Long id, RoomCreateRequest request) {

        return RoomCreateResponse.of(1L);
    }

    public void enterRoom(Long id, RoomEnterRequest request, Long roomId) {

    }

    public void deleteRoom(Long id, Long roomId) {

    }
}
