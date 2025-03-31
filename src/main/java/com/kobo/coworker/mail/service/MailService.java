package com.kobo.coworker.mail.service;

import com.kobo.coworker.mail.dto.MailDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Transactional
    public MailDto createMail(String tmpPassword, String recipientEmail) {
        String title = "[CO_Worker] 임시 비밀번호 안내";
        String message = "안녕하세요. [CO_Worker] 임시 비밀번호 안내 메일입니다.\n\n" +
                "회원님의 임시 비밀번호는 아래와 같습니다:\n\n" +
                tmpPassword + "\n\n" +
                "로그인 후 반드시 비밀번호를 변경해주세요.";

        return new MailDto(fromEmail, recipientEmail, title, message);
    }

    public void sendMail(MailDto mailDto) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(mailDto.getTo());
        mailMessage.setSubject(mailDto.getTitle());
        mailMessage.setText(mailDto.getMessage());
        mailMessage.setFrom(mailDto.getFrom());
        mailMessage.setReplyTo(mailDto.getFrom());

        mailSender.send(mailMessage);
    }

}
