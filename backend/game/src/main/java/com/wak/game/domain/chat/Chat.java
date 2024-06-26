package com.wak.game.domain.chat;

import com.wak.game.domain.room.Room;
import com.wak.game.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "chat_messages")
public class Chat extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_message_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @Column(name = "chat_message_content", nullable = false)
    private String chatMessageContent;

    @Builder
    public Chat(Room room, String chatMessageContent) {
        this.room = room;
        this.chatMessageContent = chatMessageContent;
    }
}
