package com.kobo.coworker.document.service;

import com.kobo.coworker.common.apiPayload.code.status.ErrorStatus;
import com.kobo.coworker.common.apiPayload.exception.GeneralException;
import com.kobo.coworker.common.s3.S3UploadService;
import com.kobo.coworker.document.domain.Document;
import com.kobo.coworker.document.domain.FileType;
import com.kobo.coworker.document.dto.DocumentInfoDto;
import com.kobo.coworker.document.fixture.TestFixture;
import com.kobo.coworker.document.repository.DocumentRepository;
import com.kobo.coworker.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;
import java.security.Principal;
import java.util.Optional;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class DocumentServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private DocumentRepository repository;

    @Mock
    private S3UploadService s3UploadService;


    @InjectMocks
    DocumentService service;

    private Document sampleDocument;
    private DocumentInfoDto sampleDocumentInfoDto;
    private MultipartFile sampleInvalidMultipartFile;
    private MultipartFile sampleValidMultipartFile;

    @BeforeEach
    public void init() {
        sampleDocument = TestFixture.createSampleDocument();
        sampleDocumentInfoDto = TestFixture.createSampleDocumentInfoDto();
        sampleInvalidMultipartFile = TestFixture.createSampleInvalidMultipartFile();
        sampleValidMultipartFile = TestFixture.createSampleValidMultipartFile();
    }

    @Nested
    @DisplayName("업로드 문서 관련 서비스 테스트")
    class documentTest {
//
//        @Test
//        @DisplayName("문서 타입이 유효하지 않은 경우 DOCUMENT_INVALID_FILE_TYPE 예외가 발생한다.")
//        void throwDocumentInvalidFileType_WithInvalidFileType() {
//            Principal mockPrincipal = () -> "testUser";
//            MultipartFile multipartFile = sampleInvalidMultipartFile;
//
//            assertThatThrownBy(() -> service.uploadDocument(mockPrincipal, multipartFile))
//                    .isInstanceOf(GeneralException.class)
//                    .satisfies(e -> {
//                        GeneralException ex = (GeneralException) e;
//                        assertThat(ex.getCode()).isEqualTo(ErrorStatus.DOCUMENT_INVALID_FILE_TYPE);
//                    });
//        }

        @Test
        @DisplayName("파일 경로 중복 시 DOCUMENT_ALREADY_EXISTS 예외가 발생한다.")
        void throwDocumentAlreadyExists_WithAlreadyExists() {
            String sampleFileUrl = sampleDocument.getFileUrl();
            when(repository.findByFileUrl(sampleFileUrl)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.validateDocumentFileUrlUniqueness(sampleFileUrl))
                    .isInstanceOf(GeneralException.class)
                    .satisfies(e -> {
                        GeneralException ex = (GeneralException) e;
                        assertThat(ex.getCode()).isEqualTo(ErrorStatus.DOCUMENT_ALREADY_EXISTS);
                    });
        }

//        @Test
//        @DisplayName("유효한 문서일 경우 예외 없이 업로드가 진행된다.")
//        void shouldUploadFile_WhenFileTypeIsValid() {
//            Principal mockPrincipal = () -> "testUser";
//            MultipartFile multipartFile = sampleValidMultipartFile;
//            DocumentInfoDto documentInfoDto = sampleDocumentInfoDto;
//            FileType validFileType = documentInfoDto.getFileType();
//
//            doNothing().when(userService).ensureUserIsPresent(mockPrincipal);
//            when(s3UploadService.saveFile(eq(mockPrincipal), eq(multipartFile), eq(validFileType)))
//                    .thenReturn(documentInfoDto);
//
//            DocumentInfoDto result = service.uploadDocument(mockPrincipal, multipartFile);
//
//            assertThat(result).isNotNull();
//            assertThat(result.getOriginalFilename()).isEqualTo("test.pdf");
//            assertThat(result.getFileUrl()).isEqualTo("https://coworker.s3.ap-northeast-2.amazonaws.com/세은/test.pdf");
//
//            verify(userService).ensureUserIsPresent(mockPrincipal);
//            verify(s3UploadService).saveFile(eq(mockPrincipal), eq(multipartFile), eq(validFileType));
//        }

    }
}
