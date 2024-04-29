package com.wak.game.domain.user;

import com.wak.game.domain.color.Color;
import com.wak.game.global.error.ErrorInfo;
import com.wak.game.global.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User findById(long id) {
        return userRepository.findById(id).orElseThrow(() -> new BusinessException(ErrorInfo.USER_NOT_EXIST));
    }

    public User findByNickname(String nickname) {
        return userRepository.findByNickname(nickname).orElseThrow(() -> new BusinessException(ErrorInfo.USER_NOT_EXIST));
    }

    public boolean validCheckDuplicate(String nickname) {
        if (userRepository.findByNickname(nickname).isPresent())
            throw new BusinessException(ErrorInfo.USER_ALREADY_EXIST);
        else return false;
    }

    @Transactional
    public User save(String nickName, Color color) {
        validCheckDuplicate(nickName);

        return userRepository.save(User.builder()
                .nickname(nickName)
                .color(color)
                .build());
    }


}
