package org.lc.kwengineadapter.learn_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.lc.kwengineadapter.common.entity.BaseEntity;

@Entity
@Table(name = "answers")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Answer extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(nullable = false)
    private Long authorId;

    private Integer voteCount = 0;

    @Column(nullable = false)
    private Boolean accepted = false;
}
