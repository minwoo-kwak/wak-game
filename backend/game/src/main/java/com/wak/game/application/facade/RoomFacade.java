package com.wak.game.application.facade;

import com.wak.game.application.request.RoomCreateRequest;
import com.wak.game.application.request.RoomEnterRequest;
import com.wak.game.application.response.RoomCreateResponse;
import com.wak.game.domain.room.Room;
import com.wak.game.domain.room.RoomService;
import com.wak.game.domain.user.User;
import com.wak.game.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wak.game.application.controller.dto.UserInRoomRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class RoomFacade {
    private final RoomService roomService;
    private final UserService userService;


    public RoomCreateResponse createRoom(Long id, RoomCreateRequest request) {
        User user = userService.findById(id);
        Room room = roomService.save(user, request.room_name(), request.room_password(), request.limit_players(), request.mode());
        roomService.addUserToRoom(user, room.getId(), "001", true);
        return RoomCreateResponse.of(room.getId());
    }

    public void enterRoom(Long id, RoomEnterRequest request, Long roomId) {
        User user = userService.findById(id);
        Room room = roomService.findById(roomId);
        roomService.checkPassword(room, request.room_password());
        roomService.addUserToRoom(user, room.getId(), "001", false);
    }

    public void deleteRoom(Long id, Long roomId) {
        User user = userService.findById(id);
        Room room = roomService.findById(id);
    }

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
