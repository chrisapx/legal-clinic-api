package org.lc.kwengineadapter.learn_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.lc.kwengineadapter.common.entity.BaseEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "conversations", indexes = {
        @Index(name = "idx_user_id", columnList = "user_id"),
        @Index(name = "idx_created_at", columnList = "created_at")
})
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Conversation extends BaseEntity {

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false, length = 255)
    private String title;

    @OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatMessage> messages = new ArrayList<>();

    @Column(nullable = false)
    private Boolean archived = false;

    public void addMessage(ChatMessage message) {
        messages.add(message);
        message.setConversation(this);
    }

    public void removeMessage(ChatMessage message) {
        messages.remove(message);
        message.setConversation(null);
    }
}
