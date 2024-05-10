package com.wak.game.global.util;

import com.wak.game.application.response.socket.RoomInfoResponse;
import com.wak.game.application.response.socket.RoomListResponse;
import com.wak.game.application.vo.RoomInfoVO;
import com.wak.game.application.vo.RoomVO;
import com.wak.game.domain.room.Room;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component
public class SocketUtil {
    private final SimpMessageSendingOperations simpMessageSendingOperations;
    private final RedisUtil redisUtil;

    public <T> void sendMessage(String destination, String pathVariable, T message) {
        simpMessageSendingOperations.convertAndSend("/topic" + destination + "/" + pathVariable, message);
    }

    public <T> void sendMessage(String destination, T message) {
        simpMessageSendingOperations.convertAndSend("/topic" + destination, message);
    }

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
