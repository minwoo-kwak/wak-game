package com.wak.game.domain.user;

import com.wak.game.domain.color.Color;
import com.wak.game.global.error.ErrorInfo;
import com.wak.game.global.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User findById(long id) {
        return userRepository.findById(id).orElseThrow(() -> new BusinessException(ErrorInfo.USER_NOT_EXIST));
    }

    public boolean validCheckDuplicate(String nickname, Color color) {
        if (userRepository.findByUserInfo(nickname, color) == null)
            throw new BusinessException(ErrorInfo.USER_ALREADY_EXIST);
        else return false;
    }


    public User save(String nickName, Color color) {
        validCheckDuplicate(nickName, color);

        return userRepository.save(User.builder()
                .nickname(nickName)
                .color(color)
                .build());
    }


}
