package com.wak.game.application.controller;

import com.wak.game.application.facade.RoomFacade;
import com.wak.game.application.request.RoomCreateRequest;
import com.wak.game.application.request.RoomEnterRequest;
import com.wak.game.application.response.RoomCreateResponse;
import com.wak.game.global.token.AuthUser;
import com.wak.game.global.util.ApiResult;
import com.wak.game.global.util.ApiUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.wak.game.application.request.UserInRoomRequest;
import com.wak.game.domain.color.Color;
import com.wak.game.domain.user.User;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomFacade roomFacade;

    @PostMapping("")
    public ResponseEntity<ApiResult<RoomCreateResponse>> createRoom(@AuthUser Long id, @RequestBody RoomCreateRequest request) {
        RoomCreateResponse response = roomFacade.createRoom(id, request);
        return ResponseEntity.ok(ApiUtils.success(response));
    }

    @PostMapping("/{roomId}")
    public ResponseEntity<ApiResult<Void>> enterRoom(@AuthUser Long id, @RequestBody RoomEnterRequest request ,@PathVariable("roomId") Long roomId) {
        roomFacade.enterRoom(id, request, roomId);
        return ResponseEntity.ok(ApiUtils.success(null));
    }

    @PutMapping("/{roomId}")
    public ResponseEntity<ApiResult<Void>> deleteRoom(@AuthUser Long id, @PathVariable("roomId") Long roomId) {
        roomFacade.deleteRoom(id, roomId);
        return ResponseEntity.ok(ApiUtils.success(null));
    }

    @MessageMapping("/rooms/{roomId}")
    public List<UserInRoomRequest> enterRoom(@DestinationVariable Long roomId, @Header("Authorization") String token) {
        //토큰 검증
        //User user = userFacade.findById(토큰);
        User user = User.builder()
                .color(new Color("testColor"))
                .nickname("testNickname")
                .build();

        List<UserInRoomRequest> usersInRoom = roomFacade.enter(user, roomId);


        return usersInRoom;
    }

}
