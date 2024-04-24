package com.wak.game.domain.user;

import com.wak.game.domain.color.Color;
import com.wak.game.domain.player.Player;
import com.wak.game.domain.playerLog.PlayerLog;
import com.wak.game.domain.room.Room;
import com.wak.game.domain.roomLog.RoomLog;
import com.wak.game.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "nickname")
    private String nickname;

    @OneToOne
    @JoinColumn(name = "color_id", nullable = false)
    private Color color;

    @OneToMany(mappedBy = "user")
    private List<RoomLog> roomLogs;

    @OneToMany(mappedBy = "user")
    private List<Room> rooms;

    @OneToMany(mappedBy = "user")
    private List<Player> players;

    @Builder
    public User(String nickname, Color color){
        this.nickname = nickname;
        this.color = color;
    }
}
