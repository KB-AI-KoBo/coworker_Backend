package com.kobo.coworker.user.service;

import com.kobo.coworker.common.apiPayload.code.status.ErrorStatus;
import com.kobo.coworker.common.apiPayload.exception.GeneralException;
import com.kobo.coworker.user.domain.User;
import com.kobo.coworker.user.dto.req.UpdatedUserReqDto;
import com.kobo.coworker.user.dto.req.UserSignupReqDto;
import com.kobo.coworker.user.dto.res.UserInfoDto;
import com.kobo.coworker.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.security.Principal;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserInfoDto signUp(UserSignupReqDto request) {
        ensureEmailIsUnique(request);

        User user = buildUserWithEncodedPassword(request);
        savedUser(user);

        return UserInfoDto.fromEntity(user);
    }

    private void ensureEmailIsUnique(UserSignupReqDto request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new GeneralException(ErrorStatus.USER_ALREADY_EXISTS);
        }
    }

    private User buildUserWithEncodedPassword(UserSignupReqDto request) {
        User user = request.toEntity();
        return encodePassword(user, request.getPassword());
    }

    private User encodePassword(User user, String rawPassword) {
        user.setPassword(passwordEncoder.encode(rawPassword));
        return user;
    }

    private void savedUser(User user) {
        userRepository.save(user);
    }

    @Transactional
    public UserInfoDto updateUser(Principal principal, UpdatedUserReqDto updatedUserReqDto) {
        User user = findUserWithUniqueUsername(principal.getName());

        updateFieldIfPresent(updatedUserReqDto.getUsername(), user::setUsername);
        updateFieldIfPresent(updatedUserReqDto.getPassword(), password -> user.setPassword(passwordEncoder.encode(password)));
        updateFieldIfPresent(updatedUserReqDto.getEmail(), user::setEmail);
        updateFieldIfPresent(updatedUserReqDto.getCompanyName(), user::setCompanyName);
        updateFieldIfPresent(updatedUserReqDto.getCompanySize(), user::setCompanySize);
        updateFieldIfPresent(updatedUserReqDto.getRegistrationNumber(), user::setRegistrationNumber);
        updateFieldIfPresent(updatedUserReqDto.getIndustry(), user::setIndustry);

        savedUser(user);
        return UserInfoDto.fromEntity(user);
    }

    private void updateFieldIfPresent(String value, Consumer<String> updater) {
        if (value != null && !value.trim().isEmpty()) {
            updater.accept(value.trim());
        }
    }

    public User findUserWithUniqueEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new GeneralException(ErrorStatus._UNAUTHORIZED));
    }

    public User findUserWithUniqueUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new GeneralException(ErrorStatus._UNAUTHORIZED));
    }

    public boolean isEmailRegistered(String email) {
        return userRepository.existsByEmail(email);
    }

    public String getTmpPassword() {
        char[] charSet = new char[]{ '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
                'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

        String newPassword = "";

        for (int i = 0; i < 10; i++) {
            int idx = (int) (charSet.length * Math.random());
            newPassword += charSet[idx];
        }

        return newPassword;
    }

    @Transactional
    public void updatePassword(String tmpPassword, String email) {
        String encryptedPassword = passwordEncoder.encode(tmpPassword);
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));

        user.setPassword(encryptedPassword);
    }

    public void ensureUserIsUnique(Principal principal) {
        userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_ALREADY_EXISTS));
    }
}
