package com.wak.game.application.facade;

import com.wak.game.application.controller.dto.UserInRoomRequest;
import com.wak.game.domain.room.RoomService;
import com.wak.game.domain.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class RoomFacade {
    private final RoomService roomService;

    public List<UserInRoomRequest> enter(User user, Long roomId) {
        roomService.addUserToRoom(user, roomId);
        Map<String, String> roomUsers = roomService.getUsersInRoom(roomId);

        List<UserInRoomRequest> usersInRoom = new ArrayList<>();
        roomUsers.forEach((key, value) -> {
            String[] userInfo = value.split("#");
            if (userInfo.length >= 4) {
                UserInRoomRequest userRequest = UserInRoomRequest.builder()
                        .roomId(roomId)
                        .userid(Long.parseLong(userInfo[0]))
                        .hexColor(userInfo[1])
                        .nickname(userInfo[2])
                        .team(Integer.parseInt(userInfo[3]))
                        .build();
                usersInRoom.add(userRequest);
            }
        });

        return usersInRoom;
    }
}
