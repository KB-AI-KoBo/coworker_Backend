package com.kobo.coworker.document.service;

import com.kobo.coworker.common.apiPayload.code.status.ErrorStatus;
import com.kobo.coworker.common.apiPayload.exception.GeneralException;
import com.kobo.coworker.common.s3.S3UploadService;
import com.kobo.coworker.document.domain.FileType;
import com.kobo.coworker.document.dto.DocumentInfoDto;
import com.kobo.coworker.document.repository.DocumentRepository;
import com.kobo.coworker.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final S3UploadService s3UploadService;
    private final UserService userService;
    private final DocumentRepository documentRepository;

    public DocumentInfoDto uploadDocument(String email, MultipartFile multipartFile) {
        userService.ensureUserIsPresent(email);
        validateOriginalFileNameNotNull(multipartFile);

        String extension = extractExtension(multipartFile);
        validateFileType(extension);

        return s3UploadService.saveFile(email, multipartFile, FileType.fromExtension(extension));
    }

    private void validateFileType(String extension) {
        if (!FileType.isValidExtension(extension)) {
            throw new GeneralException(ErrorStatus.DOCUMENT_INVALID_FILE_TYPE);
        }
    }

    private String extractExtension(MultipartFile multipartFile) {
        validateOriginalFileNameNotNull(multipartFile);
        return FilenameUtils.getExtension(multipartFile.getOriginalFilename());
    }

    public static void validateOriginalFileNameNotNull(MultipartFile multipartFile) {
        if (multipartFile.getOriginalFilename() == null) {
            throw new GeneralException(ErrorStatus.DOCUMENT_FILENAME_REQUIRED);
        }
    }

    public void validateDocumentFileUrlUniqueness(String fileUrl) {
        documentRepository.findByFileUrl(fileUrl)
                .orElseThrow(() -> new GeneralException(ErrorStatus.DOCUMENT_ALREADY_EXISTS));
    }

}
