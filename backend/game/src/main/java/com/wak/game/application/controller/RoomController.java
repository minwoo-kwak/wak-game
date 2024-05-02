package com.wak.game.application.controller;

import com.wak.game.application.facade.RoomFacade;
import com.wak.game.application.request.RoomCreateRequest;
import com.wak.game.application.request.RoomEnterRequest;
import com.wak.game.application.response.RoomCreateResponse;
import com.wak.game.global.token.AuthUser;
import com.wak.game.global.util.ApiResult;
import com.wak.game.global.util.ApiUtils;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<ApiResult<Void>> enterRoom(@AuthUser Long id, @RequestBody RoomEnterRequest request ,@PathVariable Long room_id) {
        roomFacade.enterRoom(id, request, room_id);
        return ResponseEntity.ok(ApiUtils.success(null));
    }

    @PutMapping("/roomId")
    public ResponseEntity<ApiResult<Void>> deleteRoom(@AuthUser Long id, @PathVariable Long room_id) {
        roomFacade.deleteRoom(id, room_id);
        return ResponseEntity.ok(ApiUtils.success(null));
    }

}
