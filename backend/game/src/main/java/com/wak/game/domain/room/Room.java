package com.wak.game.domain.room;

import com.wak.game.domain.round.Round;
import com.wak.game.domain.user.User;
import com.wak.game.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "rooms")
public class Room extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "room_name")
    private String roomName;

    @Column(name = "room_password")
    private String roomPassword;

    @Column(name = "current_players")
    private short currentPlayers;

    @Column(name = "limit_players")
    private short limitPlayers;
    private RoomType mode;

    @Column(name = "is_start")
    private boolean isStart;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @OneToMany(mappedBy = "room")
    private List<Round> rounds;

    public void updateCurrentPlayers(short currentPlayers){
        this.currentPlayers = currentPlayers;
    }

    public void updateUser(User user){
        this.user = user;
    }

    public void updateDeletedAt(){
        this.deletedAt = LocalDateTime.now();
    }

    @Builder
    public Room(User user, String roomName, String roomPassword, short limitPlayers, RoomType mode){ // mode 추가해야함.
        this.user = user;
        this.roomName = roomName;
        this.roomPassword = roomPassword;
        this.currentPlayers = 1;
        this.limitPlayers = limitPlayers;
        this.mode = mode;
        this.isStart = false;
    }

}
