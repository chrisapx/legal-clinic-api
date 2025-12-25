package org.lc.kwengineadapter.learn_service.service;

import lombok.RequiredArgsConstructor;
import org.lc.kwengineadapter.common.exception.ResourceNotFoundException;
import org.lc.kwengineadapter.learn_service.dto.AnswerRequest;
import org.lc.kwengineadapter.learn_service.dto.AnswerResponse;
import org.lc.kwengineadapter.learn_service.entity.Answer;
import org.lc.kwengineadapter.learn_service.entity.Question;
import org.lc.kwengineadapter.learn_service.repository.AnswerRepository;
import org.lc.kwengineadapter.learn_service.repository.QuestionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;

    @Transactional
    public AnswerResponse createAnswer(AnswerRequest request) {
        Question question = questionRepository.findById(request.getQuestionId())
                .orElseThrow(() -> new ResourceNotFoundException("Question", "id", request.getQuestionId()));

        Answer answer = new Answer();
        answer.setQuestion(question);
        answer.setContent(request.getContent());
        answer.setAuthorId(request.getAuthorId());
        answer.setVoteCount(0);
        answer.setAccepted(false);

        Answer savedAnswer = answerRepository.save(answer);
        return AnswerResponse.fromEntity(savedAnswer);
    }

    public AnswerResponse getAnswerById(Long id) {
        Answer answer = answerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Answer", "id", id));
        return AnswerResponse.fromEntity(answer);
    }

    public List<AnswerResponse> getAnswersByQuestion(Long questionId) {
        return answerRepository.findByQuestionId(questionId).stream()
                .map(AnswerResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public AnswerResponse updateAnswer(Long id, String content) {
        Answer answer = answerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Answer", "id", id));
        answer.setContent(content);
        Answer updatedAnswer = answerRepository.save(answer);
        return AnswerResponse.fromEntity(updatedAnswer);
    }

    @Transactional
    public AnswerResponse acceptAnswer(Long id) {
        Answer answer = answerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Answer", "id", id));
        answer.setAccepted(true);
        Answer acceptedAnswer = answerRepository.save(answer);
        return AnswerResponse.fromEntity(acceptedAnswer);
    }

    @Transactional
    public void deleteAnswer(Long id) {
        if (!answerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Answer", "id", id);
        }
        answerRepository.deleteById(id);
    }
}
