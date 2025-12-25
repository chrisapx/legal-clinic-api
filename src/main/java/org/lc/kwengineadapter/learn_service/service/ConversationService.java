package org.lc.kwengineadapter.learn_service.service;

import lombok.RequiredArgsConstructor;
import org.lc.kwengineadapter.common.exception.ResourceNotFoundException;
import org.lc.kwengineadapter.learn_service.dto.*;
import org.lc.kwengineadapter.learn_service.entity.ChatMessage;
import org.lc.kwengineadapter.learn_service.entity.Conversation;
import org.lc.kwengineadapter.learn_service.repository.ChatMessageRepository;
import org.lc.kwengineadapter.learn_service.repository.ConversationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConversationService {

    private final ConversationRepository conversationRepository;
    private final ChatMessageRepository chatMessageRepository;

    @Transactional
    public ConversationResponse createConversation(CreateConversationRequest request) {
        Conversation conversation = new Conversation();
        conversation.setUserId(request.getUserId());
        conversation.setTitle(request.getTitle());
        conversation.setArchived(false);

        Conversation saved = conversationRepository.save(conversation);
        return toConversationResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<ConversationListResponse> getUserConversations(Long userId, Boolean archived, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Conversation> conversations = conversationRepository
                .findByUserIdAndArchivedOrderByUpdatedAtDesc(userId, archived != null ? archived : false, pageable);

        return conversations.stream()
                .map(this::toConversationListResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ConversationResponse getConversationById(Long id, Long userId) {
        Conversation conversation = conversationRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Conversation not found"));

        return toConversationResponse(conversation);
    }

    @Transactional(readOnly = true)
    public List<ChatMessageResponse> getConversationMessages(Long conversationId, Long userId) {
        // Verify user owns the conversation
        conversationRepository.findByIdAndUserId(conversationId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Conversation not found"));

        List<ChatMessage> messages = chatMessageRepository.findByConversationIdOrderByCreatedAtAsc(conversationId);

        return messages.stream()
                .map(ChatMessageResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public ConversationResponse updateConversation(Long id, Long userId, UpdateConversationRequest request) {
        Conversation conversation = conversationRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Conversation not found"));

        conversation.setTitle(request.getTitle());
        Conversation updated = conversationRepository.save(conversation);

        return toConversationResponse(updated);
    }

    @Transactional
    public void archiveConversation(Long id, Long userId) {
        Conversation conversation = conversationRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Conversation not found"));

        conversation.setArchived(true);
        conversationRepository.save(conversation);
    }

    @Transactional
    public void deleteConversation(Long id, Long userId) {
        Conversation conversation = conversationRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Conversation not found"));

        conversationRepository.delete(conversation);
    }

    private ConversationResponse toConversationResponse(Conversation conversation) {
        List<ChatMessageResponse> messages = conversation.getMessages().stream()
                .map(ChatMessageResponse::fromEntity)
                .collect(Collectors.toList());

        return new ConversationResponse(
                conversation.getId(),
                conversation.getUserId(),
                conversation.getTitle(),
                conversation.getArchived(),
                conversation.getCreatedAt(),
                conversation.getUpdatedAt(),
                messages,
                messages.size()
        );
    }

    private ConversationListResponse toConversationListResponse(Conversation conversation) {
        long messageCount = chatMessageRepository.countByConversationId(conversation.getId());

        // Get the last message preview
        List<ChatMessage> messages = chatMessageRepository.findByConversationIdOrderByCreatedAtAsc(conversation.getId());
        String lastMessage = messages.isEmpty() ? "" :
                messages.get(messages.size() - 1).getUserMessage().substring(0,
                        Math.min(100, messages.get(messages.size() - 1).getUserMessage().length()));

        return new ConversationListResponse(
                conversation.getId(),
                conversation.getUserId(),
                conversation.getTitle(),
                conversation.getArchived(),
                conversation.getCreatedAt(),
                conversation.getUpdatedAt(),
                (int) messageCount,
                lastMessage
        );
    }
}
