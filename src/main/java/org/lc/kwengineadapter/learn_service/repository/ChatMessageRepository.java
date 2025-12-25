package org.lc.kwengineadapter.learn_service.repository;

import org.lc.kwengineadapter.learn_service.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByUserIdOrderByCreatedAtDesc(Long userId);
    List<ChatMessage> findByStatus(ChatMessage.MessageStatus status);
    List<ChatMessage> findByConversationIdOrderByCreatedAtAsc(Long conversationId);
    long countByConversationId(Long conversationId);
}
