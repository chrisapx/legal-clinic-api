package org.lc.kwengineadapter.learn_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.lc.kwengineadapter.learn_service.entity.Answer;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnswerResponse {
    private Long id;
    private Long questionId;
    private String content;
    private Long authorId;
    private Integer voteCount;
    private Boolean accepted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static AnswerResponse fromEntity(Answer answer) {
        return new AnswerResponse(
                answer.getId(),
                answer.getQuestion().getId(),
                answer.getContent(),
                answer.getAuthorId(),
                answer.getVoteCount(),
                answer.getAccepted(),
                answer.getCreatedAt(),
                answer.getUpdatedAt()
        );
    }
}
