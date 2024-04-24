package com.wak.game.domain.roomLog;

import com.wak.game.domain.player.Player;
import com.wak.game.domain.user.User;
import com.wak.game.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "room_logs")
public class RoomLog extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_log_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "log", nullable = false)
    private String log;


    @Builder
    public RoomLog(Long id, User user, String log) {
        this.id = id;
        this.user = user;
        this.log = log;
    }
}
