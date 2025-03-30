package com.kobo.coworker.user.dto.req;

import com.kobo.coworker.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class UserSignupReqDto {
    private String username;
    private String password;
    private String email;
    private String companyName;
    private String companySize;
    private String registrationNumber;
    private String industry;

    public User toEntity () {
        return User.builder()
                .username(username)
                .email(email)
                .companyName(companyName)
                .companySize(companySize)
                .registrationNumber(registrationNumber)
                .industry(industry)
                .build();
    }

}
