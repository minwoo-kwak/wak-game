package com.wak.game.domain.user;

import com.wak.game.domain.color.Color;

public interface UserRepositoryDSL {

    User findByUserInfo(String nickname, Color color);
    long countByNickname(String nickname);

}
