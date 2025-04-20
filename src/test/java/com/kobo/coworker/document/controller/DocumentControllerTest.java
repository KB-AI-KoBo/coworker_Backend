package com.kobo.coworker.document.controller;

import com.kobo.coworker.document.dto.DocumentInfoDto;
import com.kobo.coworker.document.fixture.TestFixture;
import com.kobo.coworker.document.service.DocumentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class DocumentControllerTest {

    @Mock
    private DocumentService service;

    @InjectMocks
    private DocumentController controller;

    private static DocumentInfoDto sampleDocumentInfoDto;
    private static MultipartFile sampleMultipartFile;

    @BeforeEach
    public void init() {
        sampleDocumentInfoDto = TestFixture.createSampleDocumentInfoDto();
        sampleMultipartFile = TestFixture.createSampleValidMultipartFile();
    }

    @Nested()
    @DisplayName("업로드 문서 관련 컨트롤러 테스트")
    class documentTest {

//        @Test
//        @DisplayName("파일 업로드 성공 시 200 OK를 반환한다.")
//        void uploadFile_Return200_OK() {
//            Principal mockPrincipal = () -> "testUser";
//            MultipartFile multipartFile = sampleMultipartFile;
//
//            when(service.uploadDocument(mockPrincipal, multipartFile)).thenReturn(sampleDocumentInfoDto);
//
//            ResponseEntity<?> response = controller.uploadFile(mockPrincipal, multipartFile);
//
//            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//            assertThat(response.getBody()).isNotNull();
//        }
    }
}
