package com.wak.game.application.facade;

import com.wak.game.application.request.RoomCreateRequest;
import com.wak.game.application.request.RoomEnterRequest;
import com.wak.game.application.response.RoomCreateResponse;
import com.wak.game.application.response.socket.RoomListResponse;
import com.wak.game.application.vo.RoomInfoVO;
import com.wak.game.application.vo.RoomVO;
import com.wak.game.domain.room.Room;
import com.wak.game.domain.room.RoomService;
import com.wak.game.domain.user.User;
import com.wak.game.domain.user.UserService;
import com.wak.game.global.error.ErrorInfo;
import com.wak.game.global.error.exception.BusinessException;
import com.wak.game.global.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wak.game.application.controller.dto.UserInRoomRequest;

import java.util.*;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class RoomFacade {
    private final RoomService roomService;
    private final UserService userService;
    private final SimpMessageSendingOperations simpMessageSendingOperations;
    private final RedisUtil redisUtil;

    /**
     * GameRoom Create Method
     *
     * @param id userId
     * @param request RoomCreateRequest
     * @return RoomCreateResponse
     */
    public RoomCreateResponse createRoom(Long id, RoomCreateRequest request) {
        User user = userService.findById(id);
        Room room = roomService.save(user, request.room_name(), request.room_password(), request.limit_players(), request.mode());
        boolean isPrivate;
        redisUtil.saveData("room" + room.getId(), String.valueOf(user.getId()), new RoomVO(user.getId(), user.getColor().getHexColor(), user.getNickname(), "001", true));
        if (room.getRoomPassword().equals("") || room.getRoomPassword() == null)
            isPrivate = false;
        else isPrivate = true;
        redisUtil.saveData("roomInfo", String.valueOf(room.getId()), new RoomInfoVO(room.getId(), room.getRoomName(), room.getCurrentPlayers(), room.getLimitPlayers(), room.getMode().toString(), false, isPrivate));
        sendRoomList();
        return RoomCreateResponse.of(room.getId());
    }

    /**
     * GameRoom Enter Method
     *
     * Error
     * 1. 입장 수 제한
     *
     * @param id
     * @param request
     * @param roomId
     */
    public void enterRoom(Long id, RoomEnterRequest request, Long roomId) {
        User user = userService.findById(id);
        Room room = roomService.findById(roomId);

        roomService.checkPassword(room, request.room_password());
        Map<String, RoomInfoVO> result = redisUtil.getData("roomInfo", RoomInfoVO.class);

        RoomInfoVO roomInfoVO = result.get(roomId.toString());
        if (roomInfoVO == null) throw new BusinessException(ErrorInfo.ROOM_NOT_EXIST);
        int curPlayer = roomInfoVO.updateCurrentPlayers();

        if (curPlayer > roomInfoVO.getLimit_players())
            throw new BusinessException(ErrorInfo.ROOM_PLAYER_IS_FULL);

        redisUtil.saveData("room" + room.getId(), String.valueOf(user.getId()), new RoomVO(user.getId(), user.getColor().getHexColor(), user.getNickname(), "001", false));
        redisUtil.saveData("roomInfo", String.valueOf(room.getId()), roomInfoVO);
        sendRoomList();
    }

    /**
     * 생성되어 있는 게임방의 리스트를 page별로 Socket 전송
     *
     */
    public void sendRoomList() {
        Map<String, RoomInfoVO> map = redisUtil.getData("roomInfo", RoomInfoVO.class);
        List<RoomInfoVO> valueList = new ArrayList<>(map.values());
        Collections.sort(valueList, (o1, o2) -> {
           if (o1.isStart() == o2.isStart())
               return Long.compare(o1.getRoom_id(), o2.getRoom_id());
           else return Boolean.compare(o1.isStart(), o2.isStart());
        });

        int size = getSize(valueList.size());
        log.info("size {}", valueList.size());

        for (int i = 1; i <= size; i++) {
            int endIndex = 0;
            if (i * 6 >= valueList.size())
                endIndex = valueList.size();
            else endIndex = i * 6;
            simpMessageSendingOperations.convertAndSend("/topic/lobby/" + i, new RoomListResponse(size, valueList.subList((i - 1) * 6, endIndex)));
        }
    }

    public int getSize(int size) {
        if (size % 6 == 0)
            return size / 6;
        else
            return (size / 6) + 1;
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