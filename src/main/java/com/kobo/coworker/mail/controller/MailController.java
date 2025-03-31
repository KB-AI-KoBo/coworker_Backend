package com.kobo.coworker.mail.controller;

import com.kobo.coworker.mail.dto.MailDto;
import com.kobo.coworker.mail.service.MailService;
import com.kobo.coworker.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mail")
@RequiredArgsConstructor
public class MailController {

    private final UserService userService;
    private final MailService mailService;

    @PostMapping("/sendPassword")
    public ResponseEntity<String> sendPassword(@RequestParam("email") String email) {
        if (!userService.isEmailRegistered(email)) {
            return ResponseEntity.badRequest().body("등록되지 않은 이메일입니다.");
        }

        String tmpPassword = userService.getTmpPassword();
        userService.updatePassword(tmpPassword, email);

        MailDto mail = mailService.createMail(tmpPassword, email);
        mailService.sendMail(mail);

        return ResponseEntity.ok("임시 비밀번호가 이메일로 전송되었습니다.");
    }

}
