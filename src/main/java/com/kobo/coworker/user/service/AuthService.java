package com.kobo.coworker.user.service;

import com.kobo.coworker.common.security.TokenProvider;
import com.kobo.coworker.user.domain.User;
import com.kobo.coworker.user.dto.req.UserLoginReqDto;
import com.kobo.coworker.user.dto.res.TokenDto;
import com.kobo.coworker.user.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    @Transactional
    public TokenDto login(UserLoginReqDto userLoginReqDto) {
        User user = userRepository.findByEmail(userLoginReqDto.getEmail())
                .orElseThrow(() -> new RuntimeException("회원가입하지 않은 사용자입니다."));

        if (!passwordEncoder.matches(userLoginReqDto.getPassword(), user.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        String accessToken = tokenProvider.createAccessToken(user.getId().toString(), user.getEmail());
        String refreshToken = tokenProvider.createRefreshToken(user.getId().toString(), user.getEmail());

        return TokenDto.builder()
                .access_token(accessToken)
                .refresh_token(refreshToken)
                .build();
    }

    @Transactional
    public TokenDto refreshAccessToken(String refreshToken) {
        if (!tokenProvider.validateToken(refreshToken)) {
            throw new RuntimeException("유효하지 않은 리프레시 토큰입니다.");
        }

        Claims claims = tokenProvider.extractClaims(refreshToken);
        String userId = claims.getSubject();
        String username = claims.get("Username", String.class);

        String newAccessToken = tokenProvider.createAccessToken(userId, username);
        return TokenDto.builder()
                .access_token(newAccessToken)
                .refresh_token(refreshToken)
                .build();
    }

}
