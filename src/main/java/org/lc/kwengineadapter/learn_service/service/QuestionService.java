package org.lc.kwengineadapter.learn_service.service;

import lombok.RequiredArgsConstructor;
import org.lc.kwengineadapter.common.exception.ResourceNotFoundException;
import org.lc.kwengineadapter.learn_service.dto.QuestionRequest;
import org.lc.kwengineadapter.learn_service.dto.QuestionResponse;
import org.lc.kwengineadapter.learn_service.entity.Question;
import org.lc.kwengineadapter.learn_service.repository.QuestionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;

    @Transactional
    public QuestionResponse createQuestion(QuestionRequest request) {
        Question question = new Question();
        question.setTitle(request.getTitle());
        question.setContent(request.getContent());
        question.setAuthorId(request.getAuthorId());
        question.setCategory(request.getCategory());
        question.setTags(request.getTags());
        question.setViewCount(0);
        question.setVoteCount(0);
        question.setResolved(false);

        Question savedQuestion = questionRepository.save(question);
        return QuestionResponse.fromEntity(savedQuestion);
    }

    public QuestionResponse getQuestionById(Long id) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Question", "id", id));
        return QuestionResponse.fromEntity(question);
    }

    public List<QuestionResponse> getAllQuestions() {
        return questionRepository.findByOrderByCreatedAtDesc().stream()
                .map(QuestionResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public List<QuestionResponse> getQuestionsByCategory(String category) {
        return questionRepository.findByCategory(category).stream()
                .map(QuestionResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public List<QuestionResponse> getUnresolvedQuestions() {
        return questionRepository.findByResolved(false).stream()
                .map(QuestionResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public QuestionResponse updateQuestion(Long id, QuestionRequest request) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Question", "id", id));

        question.setTitle(request.getTitle());
        question.setContent(request.getContent());
        question.setCategory(request.getCategory());
        question.setTags(request.getTags());

        Question updatedQuestion = questionRepository.save(question);
        return QuestionResponse.fromEntity(updatedQuestion);
    }

    @Transactional
    public QuestionResponse markAsResolved(Long id, Long acceptedAnswerId) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Question", "id", id));
        question.setResolved(true);
        question.setAcceptedAnswerId(acceptedAnswerId);
        Question resolvedQuestion = questionRepository.save(question);
        return QuestionResponse.fromEntity(resolvedQuestion);
    }

    @Transactional
    public void deleteQuestion(Long id) {
        if (!questionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Question", "id", id);
        }
        questionRepository.deleteById(id);
    }
}
