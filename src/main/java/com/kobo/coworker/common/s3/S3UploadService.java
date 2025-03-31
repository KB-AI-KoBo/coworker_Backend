package com.kobo.coworker.common.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.kobo.coworker.common.apiPayload.code.status.ErrorStatus;
import com.kobo.coworker.common.apiPayload.exception.GeneralException;
import com.kobo.coworker.document.dto.DocumentInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.security.Principal;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class S3UploadService {

    private static final int MAX_UPLOAD_SIZE = 10;
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public DocumentInfoDto saveFile(Principal principal, MultipartFile multipartFile) {
        String username = principal.getName();
        String originalFileName = validateAndGetFileName(multipartFile);
        String s3Key = generateS3Key(username, originalFileName);

        ensureUploadCapacity(username);

        uploadFileToS3(s3Key, multipartFile);
        String fileUrl = amazonS3.getUrl(bucket, s3Key).toString();

        return buildDocumentInfo(originalFileName, fileUrl);
    }

    private String validateAndGetFileName(MultipartFile multipartFile) {
        String originalFileName = multipartFile.getOriginalFilename();
        validateFileNameIsNotNullOrBlank(originalFileName);
        return originalFileName;
    }

    private void validateFileNameIsNotNullOrBlank(String originalFileName) {
        if (originalFileName == null || originalFileName.isBlank()) {
            throwFileNameRequiredException();
        }
    }

    private String generateS3Key(String username, String fileName) {
        return username + "/" + fileName;
    }

    private void throwFileNameRequiredException() {
        throw new GeneralException(ErrorStatus.DOCUMENT_FILENAME_REQUIRED);
    }

    private void ensureUploadCapacity(String username) {
        List<S3ObjectSummary> userFiles = getUserFileListFromS3(username);
        if (isUploadLimitExceeded(userFiles)) {
            removeOldestFile(userFiles);
        }
    }

    private List<S3ObjectSummary> getUserFileListFromS3(String username) {
        ObjectListing objectListing = amazonS3.listObjects(bucket, username + "/");
        return objectListing.getObjectSummaries();
    }

    private boolean isUploadLimitExceeded(List<S3ObjectSummary> userFiles) {
        return userFiles.size() >= MAX_UPLOAD_SIZE;
    }

    private void removeOldestFile(List<S3ObjectSummary> userFiles) {
        validateFileListIsNotEmpty(userFiles);
        String oldestFileKey = findOldestFileKey(userFiles);
        amazonS3.deleteObject(bucket, oldestFileKey);
    }

    private void validateFileListIsNotEmpty(List<S3ObjectSummary> userFiles) {
        if (userFiles.isEmpty()) {
            throwFileNameRequiredException();
        }
    }

    private String findOldestFileKey(List<S3ObjectSummary> userFiles) {
        return userFiles.stream()
                .min(Comparator.comparing(S3ObjectSummary::getLastModified))
                .map(S3ObjectSummary::getKey)
                .orElseThrow(this::fileNotFoundException);
    }

    private GeneralException fileNotFoundException() {
        return new GeneralException(ErrorStatus.DOCUMENT_FILENAME_REQUIRED);
    }

    private void uploadFileToS3(String s3Key, MultipartFile multipartFile) {
        try {
            ObjectMetadata metadata = createMetadata(multipartFile);
            amazonS3.putObject(bucket, s3Key, multipartFile.getInputStream(), metadata);
        } catch (IOException e) {
            throw new GeneralException(ErrorStatus._INTERNAL_SERVER_ERROR);
        }
    }

    private ObjectMetadata createMetadata(MultipartFile file) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());
        return metadata;
    }

    private DocumentInfoDto buildDocumentInfo(String originalFileName, String fileUrl) {
        return DocumentInfoDto.builder()
                .originalFilename(originalFileName)
                .fileUrl(fileUrl)
                .build();
    }
}
