package com.kobo.coworker.document.service;

import com.kobo.coworker.common.apiPayload.code.status.ErrorStatus;
import com.kobo.coworker.common.apiPayload.exception.GeneralException;
import com.kobo.coworker.common.s3.S3UploadService;
import com.kobo.coworker.document.domain.FileType;
import com.kobo.coworker.document.dto.UploadResDto;
import com.kobo.coworker.document.repository.DocumentRepository;
import com.kobo.coworker.user.service.UserService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.security.Principal;

@Service
public class DocumentService {

    private final S3UploadService s3UploadService;
    private final UserService userService;
    private final DocumentRepository documentRepository;

    @Autowired
    public DocumentService(S3UploadService s3UploadService, UserService userService, DocumentRepository documentRepository) {
        this.s3UploadService = s3UploadService;
        this.userService = userService;
        this.documentRepository = documentRepository;
    }

    public UploadResDto uploadDocument(Principal principal, MultipartFile multipartFile) {
        userService.ensureUserIsUnique(principal);
        validateFileType(multipartFile);
        return s3UploadService.saveFile(principal.getName(), multipartFile);
    }

    private void validateFileType(MultipartFile multipartFile) {
        if (!FileType.isValid(extractFileType(multipartFile))) {
            throw new GeneralException(ErrorStatus.DOCUMENT_INVALID_FILE_TYPE);
        }
    }

    private FileType extractFileType(MultipartFile multipartFile) {
        String requiredFileName = getNonNullOriginalFileName(multipartFile);
        String extension = FilenameUtils.getExtension(requiredFileName).toUpperCase();
        return FileType.valueOf(extension);
    }

    private String getNonNullOriginalFileName(MultipartFile multipartFile) {
        validateOriginalFileNameNotNull(multipartFile);
        return multipartFile.getOriginalFilename();
    }

    private void validateOriginalFileNameNotNull(MultipartFile multipartFile) {
        if (multipartFile.getOriginalFilename() == null) {
            throw new GeneralException(ErrorStatus.DOCUMENT_FILENAME_REQUIRED);
        }
    }

    public void validateDocumentFileUrlUniqueness(String fileUrl) {
        documentRepository.findByFileUrl(fileUrl)
                .orElseThrow(() -> new GeneralException(ErrorStatus.DOCUMENT_ALREADY_EXISTS));
    }
}
