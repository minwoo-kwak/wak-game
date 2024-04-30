package com.wak.game.domain.user;

import com.wak.game.domain.color.Color;

import java.util.Optional;

public interface UserRepositoryDSL {

    Optional<User> findByNickname(String nickname);
    long countByNickname(String nickname);

}
