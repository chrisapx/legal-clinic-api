package org.lc.kwengineadapter.learn_service.repository;

import org.lc.kwengineadapter.learn_service.entity.Conversation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    Page<Conversation> findByUserIdAndArchivedOrderByUpdatedAtDesc(Long userId, Boolean archived, Pageable pageable);

    List<Conversation> findByUserIdAndArchivedOrderByUpdatedAtDesc(Long userId, Boolean archived);

    Optional<Conversation> findByIdAndUserId(Long id, Long userId);

    long countByUserIdAndArchived(Long userId, Boolean archived);
}
