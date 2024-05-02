package com.wak.game.domain.room;

import com.wak.game.application.request.RoomCreateRequest;
import com.wak.game.domain.user.User;
import com.wak.game.global.error.ErrorInfo;
import com.wak.game.global.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;

    public Room findById(long id){
        return roomRepository.findById(id).orElseThrow(() -> new BusinessException(ErrorInfo.COLOR_NOT_EXIST));
    }

    public Room findByUser(User user){
        return roomRepository.findByUser(user).orElseThrow(() -> new BusinessException(ErrorInfo.ROOM_NOT_EXIST));
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





}
