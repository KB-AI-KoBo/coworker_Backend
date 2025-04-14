package com.kobo.coworker.question.service;

import com.kobo.coworker.common.apiPayload.code.status.ErrorStatus;
import com.kobo.coworker.common.apiPayload.exception.GeneralException;
import com.kobo.coworker.question.domain.Question;
import com.kobo.coworker.question.dto.QuestionInfoDto;
import com.kobo.coworker.question.fixture.TestFixture;
import com.kobo.coworker.question.repository.QuestionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class QuestionServiceTest {

    @Mock
    QuestionRepository repository;

    @InjectMocks
    QuestionService service;

    private Question sampleQuestion;
    private QuestionInfoDto sampleQuestionInfoDto;

    @BeforeEach
    public void init() {
        sampleQuestion = TestFixture.createSampleQuestion();
        sampleQuestionInfoDto = TestFixture.createSampleQuestionInfoDto();
    }

    @Nested
    @DisplayName("질문 관련 서비스 테스트")
    class questionTest {

        @Test
        @DisplayName("QuestionInfoDto -> Question 변환 시 필드 값이 정확히 매핑된다.")
        void mapFieldsCorrectly_WhenConvertingToEntity() {
            QuestionInfoDto questionInfoDto = sampleQuestionInfoDto;
            Question questionEntity = questionInfoDto.toEntity();

            assertThat(questionEntity.getDocument()).isEqualTo(questionInfoDto.getDocument());
            assertThat(questionEntity.getUser()).isEqualTo(questionInfoDto.getUser());
            assertThat(questionEntity.getContent()).isEqualTo(questionInfoDto.getContent());
        }

        @Test
        @DisplayName("존재하지 않는 질문 ID로 조회하면 QUESTION_NOT_EXISTS 예외가 발생한다.")
        void throwQuestionNotExistsException_WhenQuestionIdDoesNotExists() {
            Long nonExistsId = 999L;

            assertThatThrownBy(() -> service.ensureQuestionExistsById(nonExistsId))
                    .isInstanceOf(GeneralException.class)
                    .satisfies(e -> {
                        GeneralException ex = (GeneralException) e;
                        assertThat((ErrorStatus) ex.getCode()).isEqualTo(ErrorStatus.QUESTION_NOT_EXISTS);
                    });
        }

        @Test
        @DisplayName("존재하는 질문 ID로 조회하면 해당 질문 객체가 반환된다.")
        void returnQuestionObject_WhenQuestionIdExists() {
            Question question = sampleQuestion;

            when(repository.findById(sampleQuestion.getId())).thenReturn(Optional.of(question));

            QuestionInfoDto questionInfoDto = service.getQuestionInfoDtoById(sampleQuestion.getId());

            assertThat(questionInfoDto.getQuestionId()).isEqualTo(question.getId());
            assertThat(questionInfoDto.getContent()).isEqualTo(question.getContent());
            assertThat(questionInfoDto.getUser()).isEqualTo(question.getUser());
            assertThat(questionInfoDto.getDocument()).isEqualTo(question.getDocument());
        }
    }

}
