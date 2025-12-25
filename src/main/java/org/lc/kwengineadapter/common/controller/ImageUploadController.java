package org.lc.kwengineadapter.common.controller;

import lombok.RequiredArgsConstructor;
import org.lc.kwengineadapter.common.dto.ApiResponse;
import org.lc.kwengineadapter.common.service.StorageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/upload")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ImageUploadController {

    private final StorageService storageService;

    @PostMapping("/image")
    public ResponseEntity<ApiResponse<String>> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            if (!storageService.validateImageFile(file)) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Invalid image file. Only JPEG, PNG, GIF, and WebP are allowed."));
            }

            String imageUrl = storageService.uploadFile(file, "blog-images");
            return ResponseEntity.ok(ApiResponse.success("Image uploaded successfully", imageUrl));
        } catch (IOException e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("Failed to upload image: " + e.getMessage()));
        }
    }

    @DeleteMapping("/image")
    public ResponseEntity<ApiResponse<Void>> deleteImage(@RequestParam String url) {
        try {
            storageService.deleteFile(url);
            return ResponseEntity.ok(ApiResponse.success("Image deleted successfully", null));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("Failed to delete image: " + e.getMessage()));
        }
    }
}
