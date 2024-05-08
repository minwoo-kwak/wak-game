package com.wak.game.domain.room;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wak.game.application.vo.roomVO;
import com.wak.game.domain.user.User;
import com.wak.game.global.error.ErrorInfo;
import com.wak.game.global.error.exception.BusinessException;
import com.wak.game.global.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final RedisUtil redisUtil;

    private final RedisTemplate<String, Object> redisTemplate;

    public Room findById(Long roomId) {
        return roomRepository.findById(roomId).orElseThrow(() -> new BusinessException(ErrorInfo.ROOM_NOT_EXIST));
    }

    public Room findByUser(User user){
        return roomRepository.findByUser(user).orElseThrow(() -> new BusinessException(ErrorInfo.ROOM_NOT_EXIST));
    }

    public boolean checkPassword(Room room, String password) {
        if (room.getRoomPassword().equals("") || room.getRoomPassword() == null) return true;
        if (room.getRoomPassword().equals(password)) return true;
        else throw new BusinessException(ErrorInfo.ROOM_PASSWORD_IS_WRONG);
    }

    //C U
    public void saveObject(String key, String hashkey, Object data) {
        redisTemplate.opsForHash().put(key, hashkey, data);
    }

    public boolean isHost(User user, Room room) {

        Map<String, roomVO> usersInRoom = redisUtil.getData(String.valueOf(room.getId()), roomVO.class);

        if(usersInRoom.get(String.valueOf(user.getId())).isChief())
            return true;

        throw new BusinessException(ErrorInfo.ROOM_NOT_HOST);

    }
    //R
    public <T> Map<String, T> getData(String key, Class<T> classType) {
        Map<Object, Object> rawMap = redisTemplate.opsForHash().entries(key);
        Map<String, T> typedMap = new HashMap<>();
        for (Map.Entry<Object, Object> entry : rawMap.entrySet()) {
            typedMap.put((String) entry.getKey(), classType.cast(entry.getValue()));
        }
        return typedMap;
    }

    public Room save(User user, String roomName, String roomPassword, short limitPlayer, RoomType mode){
       if (roomRepository.findByUser(user).orElse(null) != null)
           throw new BusinessException(ErrorInfo.ROOM_ALREADY_EXIST);
        return roomRepository.save(Room.builder()
                .user(user)
                .roomName(roomName)
                .roomPassword(roomPassword)
                .limitPlayers(limitPlayer)
                .mode(mode)
                .build());
    }

    public void addUserToRoom(User user, Long roomId) {
        HashOperations<String, Object, Object> hashOps = redisTemplate.opsForHash();
        String roomKey = "room:" + roomId;
        hashOps.put(roomKey, "userId", user.getId());
        hashOps.put(roomKey, "hexColor", user.getColor());
        hashOps.put(roomKey, "nickname", user.getNickname());
        hashOps.put(roomKey, "team", "001");
        hashOps.put(roomKey, "isChief", false);
    }

    public Map<String, String> getUsersInRoom(Long roomId) {
        String roomKey = "room:" + roomId;
        HashOperations<String, String, String> hashOps = redisTemplate.opsForHash();
        return hashOps.entries(roomKey);
    }

    public void isInGame(Room room) {
        if(room.isStart())
            throw new BusinessException(ErrorInfo.ROOM_ALREADY_STARTED);
    }
}
