package com.wak.game.domain.room;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wak.game.domain.user.User;
import com.wak.game.global.error.ErrorInfo;
import com.wak.game.global.error.exception.BusinessException;
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

    private final RedisTemplate<String, Object> redisTemplate;

    public Room findById(Long roomId) {
        return roomRepository.findById(roomId).orElseThrow(() -> new BusinessException(ErrorInfo.API_ERROR_ROOM_NOT_EXIST));
    }

    public Room findByUser(User user){
        return roomRepository.findByUser(user).orElseThrow(() -> new BusinessException(ErrorInfo.ROOM_NOT_EXIST));
    }

    public boolean checkPassword(Room room, String password) {
        if (room.getRoomPassword().equals(password)) return true;
        else throw new BusinessException(ErrorInfo.ROOM_PASSWORD_IS_WRONG);
    }


    public void saveObject(String key, String hashkey, Object data) {
//        redisTemplate.opsForList().rightPush(key, data);
        redisTemplate.opsForHash().put(key, hashkey, data);
//        redisTemplate.opsForValue().set(key, data);
//        List<Object> userIds = redisTemplate.opsForList().range(key, 0, -1);
//        List<Map<Object, Object>> result = userIds.stream()
//                .map(id -> redisTemplate.opsForHash().entries("user:" + id))
//                .collect(Collectors.toList());

        // 조회
        Map<Object, Object> result = redisTemplate.opsForHash().entries(key);
        log.info("all entirys: {}" , result);

    }

    public List<Object> getObjectListByKey(String keyPattern) {
        Set<String> keys = redisTemplate.keys(keyPattern);
        if (keys == null) {
            return new ArrayList<>();
        }
        List<Object> results = keys.stream()
                .map(key -> redisTemplate.opsForValue().get(key))
                .collect(Collectors.toList());
        return results;
    }

//    public <T> boolean saveData(String key, T data){
//        try {
//            ObjectMapper mapper = new ObjectMapper();
//            String value = mapper.writeValueAsString(data);
//            redisTemplate.opsForValue().set(key, value);
//            return true;
//        } catch(Exception e){
//            log.error(String.valueOf(e));
//            return false;
//        }
//    }
//
//    public <T> Optional<T> getData(String key, Class<T> classType) {
//        String value = redisTemplate.opsForValue().get(key);
//
//        if(value == null){
//            return Optional.empty();
//        }
//
//        try {
//            ObjectMapper mapper = new ObjectMapper();
//            mapper.
//            return Optional.of(mapper.readValue(value));
//        } catch(Exception e){
//            log.error(String.valueOf(e));
//            return Optional.empty();
//        }
//    }

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

    public void addUserToRoom(User user, Long roomId, String team, boolean isChief) {
        HashOperations<String, Object, Object> hashOps = redisTemplate.opsForHash();
//        String roomKey = "room:" + String.valueOf(roomId);
        String roomKey = "room:" + roomId + ":user:" + user.getId();
        hashOps.put(roomKey, "userId", user.getId().toString());
        hashOps.put(roomKey, "hexColor", user.getColor().getHexColor());
        hashOps.put(roomKey, "nickname", user.getNickname());
        hashOps.put(roomKey, "team", team);
        hashOps.put(roomKey, "isChief", Boolean.toString(isChief));
    }

    public Map<String, String> getUsersInRoom(Long roomId) {
        String roomKey = "room:" + roomId;
        HashOperations<String, String, String> hashOps = redisTemplate.opsForHash();
        return hashOps.entries(roomKey);
    }
}


// 수정, 삭제, 조회, 넣기