package org.lc.kwengineadapter.learn_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.lc.kwengineadapter.learn_service.entity.Question;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionResponse {
    private Long id;
    private String title;
    private String content;
    private Long authorId;
    private String category;
    private String tags;
    private Integer answerCount;
    private Integer viewCount;
    private Integer voteCount;
    private Boolean resolved;
    private Long acceptedAnswerId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static QuestionResponse fromEntity(Question question) {
        return new QuestionResponse(
                question.getId(),
                question.getTitle(),
                question.getContent(),
                question.getAuthorId(),
                question.getCategory(),
                question.getTags(),
                question.getAnswers() != null ? question.getAnswers().size() : 0,
                question.getViewCount(),
                question.getVoteCount(),
                question.getResolved(),
                question.getAcceptedAnswerId(),
                question.getCreatedAt(),
                question.getUpdatedAt()
        );
    }
}
