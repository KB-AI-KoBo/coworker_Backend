package com.kobo.coworker.user.controller;

import com.kobo.coworker.common.apiPayload.ApiResponse;
import com.kobo.coworker.common.apiPayload.code.status.SuccessStatus;
import com.kobo.coworker.user.domain.User;
import com.kobo.coworker.user.dto.req.UpdatedUserReqDto;
import com.kobo.coworker.user.dto.req.UserSignupReqDto;
import com.kobo.coworker.user.dto.res.UserInfoDto;
import com.kobo.coworker.user.repository.UserRepository;
import com.kobo.coworker.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    @Autowired
    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<UserInfoDto>> signup(@Valid @RequestBody UserSignupReqDto userSignupReqDto) {
        UserInfoDto savedUser = userService.signUp(userSignupReqDto);
        return ResponseEntity.ok(ApiResponse.of(SuccessStatus._OK, savedUser));
    }

    @GetMapping("/profile")
    public ResponseEntity<User> getProfile(Principal principal) {
        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("회원가입하지 않은 사용자입니다."));

        return ResponseEntity.ok(user);
    }

    @PostMapping("/profile")
    public ResponseEntity<UserInfoDto> updateUser(Principal principal, @RequestBody UpdatedUserReqDto updatedUserReqDto) {
        UserInfoDto updatedUserInfo = userService.updateUser(principal, updatedUserReqDto);
        return ResponseEntity.ok(updatedUserInfo);
    }

    @GetMapping("/profile/username")
    public ResponseEntity<User> findByUsername(@RequestParam String username) {
        User user = userService.findByUsername(username);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/profile/email/{email}")
    public ResponseEntity<User> findByEmail(@PathVariable String email) {
        User user = userService.findByEmail(email);
        return ResponseEntity.ok(user);
    }
}
