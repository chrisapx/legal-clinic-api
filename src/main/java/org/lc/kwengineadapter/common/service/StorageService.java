package org.lc.kwengineadapter.common.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class StorageService {

    private final S3Client s3Client;

    @Value("${tebi.bucket:legalconnect}")
    private String bucketName;

    @Value("${tebi.endpoint:https://s3.tebi.io}")
    private String endpoint;

    public String uploadFile(MultipartFile file, String folder) throws IOException {
        String fileName = generateUniqueFileName(file.getOriginalFilename());
        String key = folder + "/" + fileName;

        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType(file.getContentType())
                    .acl(ObjectCannedACL.PUBLIC_READ)
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));

            String fileUrl = endpoint + "/" + bucketName + "/" + key;
            log.info("File uploaded successfully: {}", fileUrl);
            return fileUrl;
        } catch (S3Exception e) {
            log.error("Failed to upload file to Tebi.io", e);
            throw new RuntimeException("Failed to upload file: " + e.getMessage());
        }
    }

    public void deleteFile(String fileUrl) {
        try {
            String key = extractKeyFromUrl(fileUrl);

            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            s3Client.deleteObject(deleteObjectRequest);
            log.info("File deleted successfully: {}", fileUrl);
        } catch (S3Exception e) {
            log.error("Failed to delete file from Tebi.io", e);
            throw new RuntimeException("Failed to delete file: " + e.getMessage());
        }
    }

    private String generateUniqueFileName(String originalFilename) {
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        return UUID.randomUUID().toString() + extension;
    }

    private String extractKeyFromUrl(String fileUrl) {
        // Extract key from URL like: https://s3.tebi.io/legalconnect/blog-images/filename.jpg
        String baseUrl = endpoint + "/" + bucketName + "/";
        if (fileUrl.startsWith(baseUrl)) {
            return fileUrl.substring(baseUrl.length());
        }
        throw new IllegalArgumentException("Invalid file URL: " + fileUrl);
    }

    public boolean validateImageFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return false;
        }

        String contentType = file.getContentType();
        if (contentType == null) {
            return false;
        }

        return contentType.equals("image/jpeg") ||
               contentType.equals("image/png") ||
               contentType.equals("image/gif") ||
               contentType.equals("image/webp");
    }
}
