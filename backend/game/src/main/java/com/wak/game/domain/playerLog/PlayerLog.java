package com.wak.game.domain.playerLog;

import com.wak.game.domain.player.Player;
import com.wak.game.domain.user.User;
import com.wak.game.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "player_logs")
public class PlayerLog extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "player_log_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    @Column(name = "log", nullable = false)
    private String log;

    @Builder
    public PlayerLog(Player player, String log) {
        this.player = player;
        this.log = log;
    }
}
