package com.kobo.coworker.document.service;

import com.kobo.coworker.common.apiPayload.code.status.ErrorStatus;
import com.kobo.coworker.common.apiPayload.exception.GeneralException;
import com.kobo.coworker.common.s3.S3UploadService;
import com.kobo.coworker.document.domain.Document;
import com.kobo.coworker.document.domain.FileType;
import com.kobo.coworker.document.dto.UploadResDto;
import com.kobo.coworker.document.repository.DocumentRepository;
import com.kobo.coworker.user.service.UserService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.security.Principal;

@Service
public class DocumentService {

    private final S3UploadService s3UploadService;
    private final UserService userService;
    private final DocumentRepository documentRepository;

    public DocumentService(S3UploadService s3UploadService, UserService userService, DocumentRepository documentRepository) {
        this.s3UploadService = s3UploadService;
        this.userService = userService;
        this.documentRepository = documentRepository;
    }

    public UploadResDto uploadDocument(Principal principal, MultipartFile multipartFile) throws IOException {
        userService.validateUser(principal);
        validateDocument(multipartFile);
        return s3UploadService.saveFile(principal.getName(), multipartFile);
    }

    private void validateDocument(MultipartFile multipartFile) {
        FileType fileType = extractFileType(multipartFile);

        if (!FileType.isValid(fileType)) {
            throw new IllegalArgumentException("허용 되지 않은 파일 타입 : " + fileType);
        }
    }

    private FileType extractFileType(MultipartFile multipartFile) {
        String extension = FilenameUtils.getExtension(validateOriginalFilename(multipartFile)).toUpperCase();
        try {
            return FileType.valueOf(extension);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("허용 되지 않은 파일 타입 : " + extension);
        }
    }

    private String validateOriginalFilename(MultipartFile multipartFile) {
        String originalFilename = multipartFile.getOriginalFilename();
        if (originalFilename == null) {
            throw new IllegalArgumentException("파일명은 Null이 불가능합니다.");
        }
        return originalFilename;
    }

    public void findDocumentWithUniqueFileUrl(String fileUrl) {
        documentRepository.findByFileUrl(fileUrl)
                .orElseThrow(() -> new GeneralException(ErrorStatus.DOCUMENT_ALREADY_EXISTS));
    }
}
