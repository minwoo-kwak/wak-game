package com.wak.game.global.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wak.game.domain.room.dto.RoomInfo;
import com.wak.game.application.vo.RoomVO;
import com.wak.game.domain.user.User;
import com.wak.game.global.error.ErrorInfo;
import com.wak.game.global.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisUtil {
    private final RedisTemplate<String, Object> redisTemplate;

    public void saveData(String key, String hashkey, Object data) {
        redisTemplate.opsForHash().put(key, hashkey, data);
    }

    public void saveToList(String key, String value) {
        ListOperations<String, Object> listOps = redisTemplate.opsForList();
        listOps.rightPush(key, value);
    }

    public <T> Map<String, T> getData(String key, Class<T> classType) {
        Map<Object, Object> map = redisTemplate.opsForHash().entries(key);
        Map<String, T> result = new HashMap<>();
        for (Map.Entry<Object, Object> entry : map.entrySet()) {
            result.put((String) entry.getKey(), classType.cast(entry.getValue()));
        }
        return result;
    }
    public <T> List<T> getListData(String key, Class<T> classType) {
        List<Object> rawList = redisTemplate.opsForList().range(key, 0, -1);
        return rawList.stream().map(classType::cast).collect(Collectors.toList());
    }

    public void deleteKey(String key) {
        redisTemplate.delete(key);
    }

    public void deleteKey(Collection<String> keys) {
        redisTemplate.delete(keys);
    }

    public void deleteField(String key, String field) {
        redisTemplate.opsForHash().delete(key, field);
    }

    public RoomInfo getLobbyRoomInfo(Long roomId) {
        Map<String, RoomInfo> result = getData("roomInfo", RoomInfo.class);
        RoomInfo roomInfo = result.get(roomId.toString());
        if (roomInfo == null) throw new BusinessException(ErrorInfo.ROOM_NOT_EXIST_IN_REDIS);

        return roomInfo;
    }

    public RoomVO getRoomUserInfo(Long roomId, User user) {
        Map<String, RoomVO> roomVO = getData("room" + roomId, RoomVO.class);
        if (!roomVO.containsKey(user.getId().toString())) throw new BusinessException(ErrorInfo.ROOM_USER_NOT_EXIST);
        return roomVO.get(user.getId().toString());
    }

    public Map<String, RoomVO> getRoomUsersInfo(Long roomId) {
        return getData("room" + roomId, RoomVO.class);
    }

}
