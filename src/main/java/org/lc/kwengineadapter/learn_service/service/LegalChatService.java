package org.lc.kwengineadapter.learn_service.service;

import lombok.RequiredArgsConstructor;
import org.lc.kwengineadapter.common.exception.ResourceNotFoundException;
import org.lc.kwengineadapter.learn_service.dto.ChatMessageResponse;
import org.lc.kwengineadapter.learn_service.dto.ChatRequest;
import org.lc.kwengineadapter.learn_service.dto.ChatResponse;
import org.lc.kwengineadapter.learn_service.entity.ChatMessage;
import org.lc.kwengineadapter.learn_service.entity.Conversation;
import org.lc.kwengineadapter.learn_service.repository.ChatMessageRepository;
import org.lc.kwengineadapter.learn_service.repository.ConversationRepository;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LegalChatService {

    private final ChatModel chatModel;
    private final ChatMessageRepository chatMessageRepository;
    private final ConversationRepository conversationRepository;

    @Value("${spring.ai.openai.chat.options.model:gpt-4o-mini}")
    private String modelName;

    @Transactional
    public ChatMessageResponse chat(ChatRequest request) {
        String systemContext = "You are a legal assistant for the Legal Clinic Uganda platform. " +
                "Provide helpful, accurate legal information while being clear that you're providing " +
                "general information, not legal advice. Focus on Ugandan law and legal procedures.";

        // Get or create conversation
        Conversation conversation;
        if (request.getConversationId() != null) {
            conversation = conversationRepository.findByIdAndUserId(request.getConversationId(), request.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("Conversation not found"));
        } else {
            // Create new conversation with auto-generated title from first message
            conversation = new Conversation();
            conversation.setUserId(request.getUserId());
            String title = request.getMessage().substring(0, Math.min(50, request.getMessage().length()));
            conversation.setTitle(title + (request.getMessage().length() > 50 ? "..." : ""));
            conversation.setArchived(false);
            conversation = conversationRepository.save(conversation);
        }

        // Build conversation context from previous messages
        List<ChatMessage> previousMessages = chatMessageRepository.findByConversationIdOrderByCreatedAtAsc(conversation.getId());
        StringBuilder conversationContext = new StringBuilder();
        for (ChatMessage msg : previousMessages) {
            conversationContext.append("User: ").append(msg.getUserMessage()).append("\n");
            conversationContext.append("Assistant: ").append(msg.getAiResponse()).append("\n\n");
        }

        String userMessage = request.getMessage();
        if (request.getContext() != null && !request.getContext().isEmpty()) {
            userMessage = "Context: " + request.getContext() + "\n\nQuestion: " + userMessage;
        }

        String fullPrompt = systemContext + "\n\n" +
                (conversationContext.length() > 0 ? "Previous conversation:\n" + conversationContext + "\n" : "") +
                "User: " + userMessage;

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setConversation(conversation);
        chatMessage.setUserId(request.getUserId());
        chatMessage.setUserMessage(request.getMessage());
        chatMessage.setContext(request.getContext());
        chatMessage.setModel(modelName);

        String aiResponse;
        try {
            Prompt prompt = new Prompt(fullPrompt);
            aiResponse = chatModel.call(prompt).getResult().getOutput().getText();

            chatMessage.setAiResponse(aiResponse);
            chatMessage.setStatus(ChatMessage.MessageStatus.SUCCESS);
        } catch (Exception e) {
            aiResponse = "I apologize, but I'm unable to process your request at the moment. Please try again later.";
            chatMessage.setAiResponse(aiResponse);
            chatMessage.setStatus(ChatMessage.MessageStatus.FAILED);
            chatMessage.setErrorMessage(e.getMessage());
        }

        // Save the chat message to database
        ChatMessage savedMessage = chatMessageRepository.save(chatMessage);

        return ChatMessageResponse.fromEntity(savedMessage);
    }
}
