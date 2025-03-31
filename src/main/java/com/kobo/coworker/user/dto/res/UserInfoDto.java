package com.kobo.coworker.user.dto.res;

import com.kobo.coworker.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class UserInfoDto {

    private Long id;
    private String username;
    private String email;
    private String companyName;
    private String companySize;
    private String registrationNumber;
    private String industry;

    public static UserInfoDto fromEntity(User user) {
        return UserInfoDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .companyName(user.getCompanyName())
                .companySize(user.getCompanySize())
                .registrationNumber(user.getRegistrationNumber())
                .build();
    }

}
