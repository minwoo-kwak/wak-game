package com.wak.game.application.controller;

import com.wak.game.application.facade.RoomFacade;
import com.wak.game.application.request.RoomCreateRequest;
import com.wak.game.application.request.RoomEnterRequest;
import com.wak.game.application.response.RoomBasicInfoResponse;
import com.wak.game.application.response.RoomCreateResponse;
import com.wak.game.application.response.UserInfoResponse;
import com.wak.game.global.error.ErrorInfo;
import com.wak.game.global.token.AuthUser;
import com.wak.game.global.util.ApiErrorExamples;
import com.wak.game.global.util.ApiResult;
import com.wak.game.global.util.ApiUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.wak.game.application.controller.dto.UserInRoomRequest;
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

    @Operation(
            summary = "Room 게임룸 생성",
            description = "Room 게임룸을 생성하는 API 입니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "생성 성공", content = @Content(schema = @Schema(implementation = RoomCreateResponse.class)))
            },
            security = { @SecurityRequirement(name = "Access-Token") }
    )
    @ApiErrorExamples({ErrorInfo.ROOM_USER_ALREADY_EXIST, ErrorInfo.ROOM_NOT_EXIST_IN_REDIS, ErrorInfo.ROOM_PLAYER_IS_FULL})
    @PostMapping("")
    public ResponseEntity<ApiResult<RoomCreateResponse>> createRoom(@AuthUser Long id, @RequestBody RoomCreateRequest request) {
        RoomCreateResponse response = roomFacade.createRoom(id, request);
        return ResponseEntity.ok(ApiUtils.success(response));
    }

    @Operation(
            summary = "Room 게임룸 입장",
            description = "Room 게임룸에 입장하는 API 입니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "입장 성공")
            },
            security = { @SecurityRequirement(name = "Access-Token") }
    )
    @ApiErrorExamples({ErrorInfo.ROOM_USER_ALREADY_EXIST, ErrorInfo.ROOM_NOT_EXIST_IN_REDIS, ErrorInfo.ROOM_PLAYER_IS_FULL})
    @PostMapping("/{roomId}")
    public ResponseEntity<ApiResult<Void>> enterRoom(@AuthUser Long id, @RequestBody RoomEnterRequest request ,@PathVariable("roomId") Long roomId) {
        roomFacade.enterRoom(id, request, roomId);
        return ResponseEntity.ok(ApiUtils.success(null));
    }

    @Operation(
            summary = "Room 게임룸 퇴장",
            description = "Room 게임룸에 퇴장하는 API 입니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "퇴장 성공")
            },
            security = { @SecurityRequirement(name = "Access-Token") }
    )
    @ApiErrorExamples({ErrorInfo.ROOM_USER_NOT_EXIST, ErrorInfo.ROOM_NOT_EXIST_IN_REDIS, ErrorInfo.ROOM_PLAYER_IS_EMPTY})
    @PutMapping("/{roomId}")
    public ResponseEntity<ApiResult<Void>> exitRoom(@AuthUser Long id, @PathVariable("roomId") Long roomId) {
        roomFacade.exitRoom(id, roomId);
        return ResponseEntity.ok(ApiUtils.success(null));
    }

    @Operation(
            summary = "Room 게임룸 입장 시 publish 요청",
            description = "Room 게임룸 입장 시 게임룸에 관한 정보를 publish 요청하는 API 입니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "입장 성공")
            },
            security = { @SecurityRequirement(name = "Access-Token") }
    )
    @ApiErrorExamples({ErrorInfo.ROOM_USER_NOT_EXIST})
    @GetMapping("/topic/{roomId}")
    public ResponseEntity<ApiResult<RoomBasicInfoResponse>> publishRoomInfo(@AuthUser Long id, @PathVariable("roomId") Long roomId) {
        RoomBasicInfoResponse response = roomFacade.sendRoomInfo(id, roomId);
        return ResponseEntity.ok(ApiUtils.success(response));
    }

    @Operation(
            summary = "로비 입장 시 publish 요청",
            description = "로비 입장 시 게임룸 리스트에 대한 정보를 publish 요청하는 API 입니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "입장 성공")
            },
            security = { @SecurityRequirement(name = "Access-Token") }
    )
    @GetMapping("/topic/lobby")
    public ResponseEntity<ApiResult<Void>> publishLobbyInfo() {
        roomFacade.sendRoomList();
        return ResponseEntity.ok(ApiUtils.success(null));
    }

}
