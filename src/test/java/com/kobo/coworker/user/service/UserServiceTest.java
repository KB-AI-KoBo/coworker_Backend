package com.kobo.coworker.user.service;

import com.kobo.coworker.common.apiPayload.code.status.ErrorStatus;
import com.kobo.coworker.common.apiPayload.exception.GeneralException;
import com.kobo.coworker.user.domain.User;
import com.kobo.coworker.user.dto.req.UserSignupReqDto;
import com.kobo.coworker.user.fixture.TestFixture;
import com.kobo.coworker.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository repository;

    @Spy
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    UserService service;

    private User sampleUser;
    private UserSignupReqDto sampleUserSignupReqDto;

    @BeforeEach
    void setUp() {
        sampleUser = TestFixture.createSampleUser();
        sampleUserSignupReqDto = TestFixture.createSampleUserSignupReqDto();
    }

    @Nested
    @DisplayName("회원가입 관련 서비스 테스트")
    class signUpTest {

        @Test
        @DisplayName("이미 존재하는 이메일로 회원가입 시 USER_ALREADY_EXISTS 예외가 발생한다.")
        void signUp_WithDuplicateEmail_ThrowsUserAlreadyExists() {
            User user = sampleUser;

            when(repository.findByEmail(sampleUser.getEmail())).thenReturn(Optional.of(user));

            assertThatThrownBy(() -> service.signUp(sampleUserSignupReqDto))
                    .isInstanceOf(GeneralException.class)
                    .satisfies(e -> {
                        GeneralException ex = (GeneralException) e;
                        assertThat((ErrorStatus) ex.getCode()).isEqualTo(ErrorStatus.USER_ALREADY_EXISTS);
                    });
        }

        @Test
        @DisplayName("회원가입 시 비밀번호는 passwordEncoder를 통해 인코딩된다.")
        void signUp_ShouldCallPasswordEncoderEncode() {
            service.signUp(sampleUserSignupReqDto);
            String rawPassword = sampleUserSignupReqDto.getPassword();
            verify(passwordEncoder).encode(rawPassword);
        }
    }
}
