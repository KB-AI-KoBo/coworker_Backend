package com.kobo.coworker.user.fixture;

import com.kobo.coworker.user.domain.User;
import com.kobo.coworker.user.dto.req.UserSignupReqDto;

public class TestFixture {

    public static UserSignupReqDto createSampleUserSignupReqDto() {
        return UserSignupReqDto.builder()
                .username("세은")
                .password("3034")
                .email("seunpark04@gmail.com")
                .companyName("KB")
                .companySize("VERY_LARGE")
                .registrationNumber("12345")
                .industry("IT")
                .build();
    }

    public static User createSampleUser() {
        return User.builder()
                .username("세은")
                .password("3034")
                .email("seunpark04@gmail.com")
                .companyName("KB")
                .companySize("VERY_LARGE")
                .registrationNumber("12345")
                .industry("IT")
                .build();
    }
}
