package com.kobo.coworker.user.dto.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class UpdatedUserReqDto {

    private String username;
    private String password;
    private String email;
    private String companyName;
    private String companySize;
    private String registrationNumber;
    private String industry;

}
