package com.kobo.coworker.question.controller;

import com.kobo.coworker.common.apiPayload.code.status.SuccessStatus;
import com.kobo.coworker.question.dto.QuestionInfoDto;
import com.kobo.coworker.question.fixture.TestFixture;
import com.kobo.coworker.question.service.QuestionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
public class QuestionControllerTest {

    @Mock
    private QuestionService service;

    @InjectMocks
    private QuestionController controller;

    private static QuestionInfoDto sampleQuestionInfoDto;

    @BeforeEach
    public void init() {
        sampleQuestionInfoDto = TestFixture.createSampleQuestionInfoDto();
    }

    @Nested
    @DisplayName("질문 관련 컨트롤러 테스트")
    class questionTest {

        @Test
        @DisplayName("질문 id 조회 시 해당 id의 QuestionInfoDto가 반환된다.")
        void getQuestionInfoDtoById_returnsMatchingDto() {

        }

        @Test
        @DisplayName("질문 삭제 성공 시 SuccessStatus 반환")
        void deleteQuestion_ReturnsSuccessStatus() {
            QuestionInfoDto questionInfoDto = sampleQuestionInfoDto;

            doNothing().when(service).deleteQuestion(questionInfoDto.getId());

            SuccessStatus status = controller.deleteQuestion(questionInfoDto.getId());

            verify(service).deleteQuestion(questionInfoDto.getId());
            assertThat(status).isEqualTo(SuccessStatus._OK);
        }
    }
}
