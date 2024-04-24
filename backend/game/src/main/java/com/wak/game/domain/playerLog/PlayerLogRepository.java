package com.wak.game.domain.playerLog;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerLogRepository extends JpaRepository<PlayerLog, Long> {

}
