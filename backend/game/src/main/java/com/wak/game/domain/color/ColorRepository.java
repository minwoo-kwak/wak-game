package com.wak.game.domain.color;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.awt.*;
import java.util.Optional;

@Repository
public interface ColorRepository extends JpaRepository<Color, Long>, ColorRepositoryCustom {

}
