package com.wak.game.application.controller;

import com.wak.game.application.facade.RankFacade;
import com.wak.game.application.response.RoomCreateResponse;
import com.wak.game.application.response.socket.RoomListResponse;
import com.wak.game.domain.rank.RankService;
import com.wak.game.domain.room.dto.RoomInfo;
import com.wak.game.global.error.ErrorInfo;
import com.wak.game.global.util.ApiErrorExamples;
import com.wak.game.global.util.ApiResult;
import com.wak.game.global.util.ApiUtils;
import com.wak.game.global.util.SocketUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Map;

@RestController
@RequestMapping("/api/ranks")
@RequiredArgsConstructor
public class RankingController {
    private final RankFacade rankFacade;

    @Operation(
            summary = "실시간 랭킹정보 반환",
            description = "게임 중 실시간 랭킹을 반환하는 API 입니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "반환 성공")
            },
            security = {@SecurityRequirement(name = "Access-Token")}
    )
    @GetMapping("/topic/games/{round-id}/rank")
    public ResponseEntity<ApiResult<Void>> publishRank(@PathVariable("round-id") Long roundId) {
        rankFacade.sendRank(roundId);
        return ResponseEntity.ok(ApiUtils.success(null));
    }
}
