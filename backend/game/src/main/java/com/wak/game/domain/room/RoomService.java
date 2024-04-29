package com.wak.game.domain.room;

import com.wak.game.domain.user.User;
import com.wak.game.global.error.ErrorInfo;
import com.wak.game.global.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final RedisTemplate<String, String> redisTemplate;

    private final RoomRepository roomRepository;
    public Room findById(Long roomId) {
        return roomRepository.findById(roomId).orElseThrow(() -> new BusinessException(ErrorInfo.API_ERROR_ROOM_NOT_EXIST));
    }

    public void addUserToRoom(User user, Long roomId) {
        HashOperations<String, Object, Object> hashOps = redisTemplate.opsForHash();
        String roomKey = "room:" + roomId;
        hashOps.put(roomKey, "userId", user.getId());
        hashOps.put(roomKey, "hexColor", "testcolor");
        hashOps.put(roomKey, "nickname", "testNickname");
        hashOps.put(roomKey, "team", "001");
        hashOps.put(roomKey, "isChief", false);
    }

    public Map<String, String> getUsersInRoom(Long roomId) {
        String roomKey = "room:" + roomId;
        HashOperations<String, String, String> hashOps = redisTemplate.opsForHash();
        return hashOps.entries(roomKey);
    }
}
