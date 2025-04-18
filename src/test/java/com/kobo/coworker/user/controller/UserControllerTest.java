package com.kobo.coworker.user.controller;

import com.kobo.coworker.common.apiPayload.ApiResponse;
import com.kobo.coworker.user.dto.req.UserSignupReqDto;
import com.kobo.coworker.user.dto.res.UserInfoDto;
import com.kobo.coworker.user.fixture.TestFixture;
import com.kobo.coworker.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService service;

    @InjectMocks
    private UserController controller;

    private static UserSignupReqDto sampleUser;

    @BeforeEach
    void setUp() {
        sampleUser = TestFixture.createSampleUserSignupReqDto();
    }

    @Nested
    @DisplayName("회원가입 관련 컨트롤러 테스트")
    class signUpTest {

        @Test
        @DisplayName("회원가입 성공 시 200 OK 및 isSuccess=true 반환")
        void signUp_ReturnsStatus200AndSuccess() {
            UserSignupReqDto userSignupReqDto = sampleUser;
            UserInfoDto userInfoDto = UserInfoDto.fromEntity(userSignupReqDto.toEntity());

            when(service.signUp(userSignupReqDto)).thenReturn(userInfoDto);

            ResponseEntity<ApiResponse<UserInfoDto>> response = controller.signup(userSignupReqDto);

            assertThat(response.getStatusCode().value()).isEqualTo(200);
            assertThat(response.getBody().getIsSuccess()).isTrue();
        }

    }

}
