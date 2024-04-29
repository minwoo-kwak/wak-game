package com.wak.game.application.controller;

import com.wak.game.application.controller.dto.UserInRoomRequest;
import com.wak.game.application.facade.RoomFacade;
import com.wak.game.domain.color.Color;
import com.wak.game.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/room")
@RequiredArgsConstructor
public class RoomController {
    private final RoomFacade roomFacade;
    @MessageMapping("/rooms/{roomId}")
    public  List<UserInRoomRequest> enterRoom(@DestinationVariable Long roomId, @Header("Authorization") String token) {
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
