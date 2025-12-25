package org.lc.kwengineadapter.learn_service.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.lc.kwengineadapter.common.dto.ApiResponse;
import org.lc.kwengineadapter.learn_service.dto.AnswerRequest;
import org.lc.kwengineadapter.learn_service.dto.AnswerResponse;
import org.lc.kwengineadapter.learn_service.service.AnswerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/answers")
@RequiredArgsConstructor
public class AnswerController {

    private final AnswerService answerService;

    @PostMapping
    public ResponseEntity<ApiResponse<AnswerResponse>> createAnswer(@Valid @RequestBody AnswerRequest request) {
        AnswerResponse answer = answerService.createAnswer(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Answer created successfully", answer));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AnswerResponse>> getAnswerById(@PathVariable Long id) {
        AnswerResponse answer = answerService.getAnswerById(id);
        return ResponseEntity.ok(ApiResponse.success(answer));
    }

    @GetMapping("/question/{questionId}")
    public ResponseEntity<ApiResponse<List<AnswerResponse>>> getAnswersByQuestion(@PathVariable Long questionId) {
        List<AnswerResponse> answers = answerService.getAnswersByQuestion(questionId);
        return ResponseEntity.ok(ApiResponse.success(answers));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AnswerResponse>> updateAnswer(
            @PathVariable Long id,
            @RequestBody String content) {
        AnswerResponse answer = answerService.updateAnswer(id, content);
        return ResponseEntity.ok(ApiResponse.success("Answer updated successfully", answer));
    }

    @PatchMapping("/{id}/accept")
    public ResponseEntity<ApiResponse<AnswerResponse>> acceptAnswer(@PathVariable Long id) {
        AnswerResponse answer = answerService.acceptAnswer(id);
        return ResponseEntity.ok(ApiResponse.success("Answer accepted successfully", answer));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteAnswer(@PathVariable Long id) {
        answerService.deleteAnswer(id);
        return ResponseEntity.ok(ApiResponse.success("Answer deleted successfully", null));
    }
}
