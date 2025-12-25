package org.lc.kwengineadapter.learn_service.repository;

import org.lc.kwengineadapter.learn_service.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
    List<Answer> findByQuestionId(Long questionId);
    List<Answer> findByAuthorId(Long authorId);
    List<Answer> findByAccepted(Boolean accepted);
}
