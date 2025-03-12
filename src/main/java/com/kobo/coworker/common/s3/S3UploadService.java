package com.kobo.coworker.common.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.kobo.coworker.document.dto.UploadResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class S3UploadService {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public UploadResDto saveFile(String username, MultipartFile multipartFile) throws IOException {
        String originalFilename = validateOriginalFilename(multipartFile);
        String s3Key = username + "/" + originalFilename;

        List<S3ObjectSummary> userFiles = getUserFiles(username);

        if (userFiles.size() >= 10) {
            deleteOldestFile(userFiles);
        }

        ObjectMetadata metadata = setMetaData(multipartFile);
        amazonS3.putObject(bucket, s3Key, multipartFile.getInputStream(), metadata);
        String fileUrl = amazonS3.getUrl(bucket, s3Key).toString();

        return new UploadResDto(originalFilename, fileUrl);
    }

    private void deleteOldestFile(List<S3ObjectSummary> userFiles) {
        if (userFiles.isEmpty()) return;

        userFiles.sort(Comparator.comparing(S3ObjectSummary::getLastModified));

        String oldestFileKey = userFiles.get(0).getKey();
        amazonS3.deleteObject(bucket, oldestFileKey);
        System.out.println("오래된 문서 삭제 : " + oldestFileKey);
    }

    private List<S3ObjectSummary> getUserFiles(String username) {
        ObjectListing objectListing = amazonS3.listObjects(bucket, username + "/");
        return objectListing.getObjectSummaries();
    }

    private String validateOriginalFilename(MultipartFile multipartFile) {
        String originalFilename = multipartFile.getOriginalFilename();
        if (originalFilename == null || originalFilename.isEmpty()) {
            throw new IllegalArgumentException("파일명은 Null이 불가능합니다.");
        }
        return originalFilename;
    }

    private ObjectMetadata setMetaData(MultipartFile multipartFile) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());
        return metadata;
    }
}
