package org.lc.kwengineadapter.learn_service.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.lc.kwengineadapter.common.dto.ApiResponse;
import org.lc.kwengineadapter.learn_service.dto.QuestionRequest;
import org.lc.kwengineadapter.learn_service.dto.QuestionResponse;
import org.lc.kwengineadapter.learn_service.service.QuestionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    @PostMapping
    public ResponseEntity<ApiResponse<QuestionResponse>> createQuestion(@Valid @RequestBody QuestionRequest request) {
        QuestionResponse question = questionService.createQuestion(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Question created successfully", question));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<QuestionResponse>> getQuestionById(@PathVariable Long id) {
        QuestionResponse question = questionService.getQuestionById(id);
        return ResponseEntity.ok(ApiResponse.success(question));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<QuestionResponse>>> getAllQuestions() {
        List<QuestionResponse> questions = questionService.getAllQuestions();
        return ResponseEntity.ok(ApiResponse.success(questions));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<ApiResponse<List<QuestionResponse>>> getQuestionsByCategory(@PathVariable String category) {
        List<QuestionResponse> questions = questionService.getQuestionsByCategory(category);
        return ResponseEntity.ok(ApiResponse.success(questions));
    }

    @GetMapping("/unresolved")
    public ResponseEntity<ApiResponse<List<QuestionResponse>>> getUnresolvedQuestions() {
        List<QuestionResponse> questions = questionService.getUnresolvedQuestions();
        return ResponseEntity.ok(ApiResponse.success(questions));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<QuestionResponse>> updateQuestion(
            @PathVariable Long id,
            @Valid @RequestBody QuestionRequest request) {
        QuestionResponse question = questionService.updateQuestion(id, request);
        return ResponseEntity.ok(ApiResponse.success("Question updated successfully", question));
    }

    @PatchMapping("/{id}/resolve")
    public ResponseEntity<ApiResponse<QuestionResponse>> markAsResolved(
            @PathVariable Long id,
            @RequestParam Long acceptedAnswerId) {
        QuestionResponse question = questionService.markAsResolved(id, acceptedAnswerId);
        return ResponseEntity.ok(ApiResponse.success("Question marked as resolved", question));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteQuestion(@PathVariable Long id) {
        questionService.deleteQuestion(id);
        return ResponseEntity.ok(ApiResponse.success("Question deleted successfully", null));
    }
}
