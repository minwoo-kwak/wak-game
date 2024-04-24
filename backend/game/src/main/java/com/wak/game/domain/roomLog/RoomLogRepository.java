package com.wak.game.domain.roomLog;

import com.wak.game.domain.playerLog.PlayerLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomLogRepository extends JpaRepository<RoomLog, Long> {

}
