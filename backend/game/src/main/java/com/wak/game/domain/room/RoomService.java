package com.wak.game.domain.room;

import com.wak.game.domain.user.User;
import com.wak.game.global.error.ErrorInfo;
import com.wak.game.global.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

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
        if (room.getRoomPassword().equals("") || room.getRoomPassword() == null) return true;
        if (room.getRoomPassword().equals(password)) return true;
        else throw new BusinessException(ErrorInfo.ROOM_PASSWORD_IS_WRONG);
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
}
