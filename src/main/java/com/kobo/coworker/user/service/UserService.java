package com.kobo.coworker.user.service;

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
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("이미 존재하는 이메일입니다.");
        }

        User user = request.toEntity();
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);

        return UserInfoDto.fromEntity(user);
    }

    @Transactional
    public UserInfoDto updateUser(Principal principal, UpdatedUserReqDto updatedUserReqDto) {
        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));

        updateFieldIfPresent(updatedUserReqDto.getUsername(), user::setUsername);
        updateFieldIfPresent(updatedUserReqDto.getPassword(), password -> user.setPassword(passwordEncoder.encode(password)));
        updateFieldIfPresent(updatedUserReqDto.getEmail(), user::setEmail);
        updateFieldIfPresent(updatedUserReqDto.getCompanyName(), user::setCompanyName);
        updateFieldIfPresent(updatedUserReqDto.getCompanySize(), user::setCompanySize);
        updateFieldIfPresent(updatedUserReqDto.getRegistrationNumber(), user::setRegistrationNumber);
        updateFieldIfPresent(updatedUserReqDto.getIndustry(), user::setIndustry);

        userRepository.save(user);
        return UserInfoDto.fromEntity(user);
    }

    private void updateFieldIfPresent(String value, Consumer<String> updater) {
        if (value != null && !value.trim().isEmpty()) {
            updater.accept(value.trim());
        }
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));
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

    public void validateUser(Principal principal) {
        String username = principal.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("현재 사용자를 찾을 수 없습니다. username : " + username));
    }
}
