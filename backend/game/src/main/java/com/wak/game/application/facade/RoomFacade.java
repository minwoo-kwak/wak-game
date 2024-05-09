package com.wak.game.application.facade;

import com.wak.game.application.request.RoomCreateRequest;
import com.wak.game.application.request.RoomEnterRequest;
import com.wak.game.application.response.RoomBasicInfoResponse;
import com.wak.game.application.response.RoomCreateResponse;
import com.wak.game.application.response.socket.RoomInfoResponse;
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
        Room room = roomService.save(user, request.roomName(), request.roomPassword(), request.limitPlayers(), request.mode());
        boolean isPublic;

        if (room.getRoomPassword().equals("") || room.getRoomPassword() == null) isPublic = true;
        else isPublic = false;

        redisUtil.saveData("room" + room.getId(), String.valueOf(user.getId()), new RoomVO(user.getId(), user.getColor().getHexColor(), user.getNickname(), "001", true));
        redisUtil.saveData("roomInfo", String.valueOf(room.getId()), new RoomInfoVO(room.getId(), room.getRoomName(), room.getCurrentPlayers(), room.getLimitPlayers(), room.getMode().toString(), false, isPublic));

        sendRoomList();
        sendRoomInfoSocket(room);
        return RoomCreateResponse.of(room.getId());
    }

    /**
     * GameRoom Enter Method
     *
     * Error
     * 1. 입장 수 제한
     * 2. 유저 중복 체크
     *
     * @param id
     * @param request
     * @param roomId
     */
    public void enterRoom(Long id, RoomEnterRequest request, Long roomId) {
        User user = userService.findById(id);
        Room room = roomService.findById(roomId);

        roomService.checkPassword(room, request.roomPassword());
        Map<String, RoomVO> roomVO = redisUtil.getData("room" + roomId, RoomVO.class);
        if (roomVO.containsKey(user.getId().toString())) throw new BusinessException(ErrorInfo.ROOM_USER_ALREADY_EXIST);

        RoomInfoVO roomInfoVO = getLobbyRoomInfo(roomId);
        if (roomInfoVO.getIsStart()) throw new BusinessException(ErrorInfo.ROOM_IS_START);

        int curPlayer = roomInfoVO.updateCurrentPlayers();
        if (curPlayer > roomInfoVO.getLimitPlayers())
            throw new BusinessException(ErrorInfo.ROOM_PLAYER_IS_FULL);

        redisUtil.saveData("room" + room.getId(), String.valueOf(user.getId()), new RoomVO(user.getId(), user.getColor().getHexColor(), user.getNickname(), "001", false));
        redisUtil.saveData("roomInfo", String.valueOf(room.getId()), roomInfoVO);

        sendRoomList();
        sendRoomInfoSocket(room);
    }


    /**
     * GameRoom Exit Method
     *
     * @param id
     * @param roomId
     */
    public void exitRoom(Long id, Long roomId) {
        User user = userService.findById(id);
        Room room = roomService.findById(roomId);

        RoomVO userRoom = getRoomUserInfo(roomId, user);

        if (userRoom.isHost()) {
            deleteRoom(room);
        } else {
            RoomInfoVO roomInfoVO = getLobbyRoomInfo(roomId);

            int curPlayer = roomInfoVO.decreaseCurrentPlayers();
            if (curPlayer <= 0)
                throw new BusinessException(ErrorInfo.ROOM_PLAYER_IS_EMPTY);

            redisUtil.deleteField("room" + room.getId(), String.valueOf(user.getId()));
            redisUtil.saveData("roomInfo", String.valueOf(room.getId()), roomInfoVO);
            sendRoomList();
            sendRoomInfoSocket(room);
        }
    }

    /**
     * GameRoom Delete Method
     *
     * @param room
     */
    public void deleteRoom(Room room){
        redisUtil.deleteKey("room" + room.getId());
        redisUtil.deleteField("roomInfo", String.valueOf(room.getId()));
        roomService.deleteRoom(room);
        simpMessageSendingOperations.convertAndSend("/topic/rooms/" + room.getId(), "ROOM IS EXPIRED");
    }

    public RoomBasicInfoResponse sendRoomInfo(Long userId, Long roomId) {
        User user = userService.findById(userId);
        Room room = roomService.findById(roomId);

        RoomVO userRoom = getRoomUserInfo(roomId, user);
        RoomInfoVO roomInfoVO = getLobbyRoomInfo(roomId);

        sendRoomInfoSocket(room);
        return RoomBasicInfoResponse.of(userId, userRoom.isHost(), roomId, room.getRoomName(), room.getMode().toString(), room.getLimitPlayers(),roomInfoVO.getIsPublic());
    }

    /**
     * 생성되어 있는 게임방의 리스트를 page별로 Socket 전송
     *
     */
    public void sendRoomList() {
        Map<String, RoomInfoVO> map = redisUtil.getData("roomInfo", RoomInfoVO.class);
        List<RoomInfoVO> valueList = new ArrayList<>(map.values());
        Collections.sort(valueList, (o1, o2) -> {
           if (o1.getIsStart() == o2.getIsStart())
               return -Long.compare(o1.getRoomId(), o2.getRoomId());
           else return Boolean.compare(o1.getIsStart(), o2.getIsStart());
        });

        int size = getSize(valueList.size());

        for (int i = 1; i <= size; i++) {
            int endIndex = 0;
            if (i * 6 >= valueList.size())
                endIndex = valueList.size();
            else endIndex = i * 6;
            simpMessageSendingOperations.convertAndSend("/topic/lobby/" + i, new RoomListResponse(size, valueList.subList((i - 1) * 6, endIndex)));
        }

        if (size == 0)
            simpMessageSendingOperations.convertAndSend("/topic/lobby/" + 1, new RoomListResponse(size, null));
    }

    public RoomInfoVO getLobbyRoomInfo(Long roomId) {
        Map<String, RoomInfoVO> result = redisUtil.getData("roomInfo", RoomInfoVO.class);
        RoomInfoVO roomInfoVO = result.get(roomId.toString());
        if (roomInfoVO == null) throw new BusinessException(ErrorInfo.ROOM_NOT_EXIST_IN_REDIS);

        return roomInfoVO;
    }

    public RoomVO getRoomUserInfo(Long roomId, User user) {
        Map<String, RoomVO> roomVO = redisUtil.getData("room" + roomId, RoomVO.class);
        if (!roomVO.containsKey(user.getId().toString())) throw new BusinessException(ErrorInfo.ROOM_USER_NOT_EXIST);
        return roomVO.get(user.getId().toString());
    }

    public void gameStart(Room room) {

    }

    public void gameFinish(Room room) {

    }

    public void sendRoomInfoSocket(Room room) {
        Map<String, RoomInfoVO> roominfo = redisUtil.getData("roomInfo", RoomInfoVO.class);
        RoomInfoVO roomInfoVO = roominfo.get(room.getId().toString());

        Map<String, RoomVO> userinfo = redisUtil.getData("room" + room.getId() , RoomVO.class);
        List<RoomVO> users = new ArrayList<>(userinfo.values());

        simpMessageSendingOperations.convertAndSend("/topic/rooms/" + room.getId(), new RoomInfoResponse(room.getId(), roomInfoVO.getCurrentPlayers(), room.getUser().getId(), roomInfoVO.getIsStart(), users));
    }

    public int getSize(int size) {
        if (size % 6 == 0)
            return size / 6;
        else
            return (size / 6) + 1;
    }

}