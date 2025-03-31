package com.kobo.coworker.user.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import javax.validation.constraints.Email;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    @Email(message = "올바른 이메일 형식이어야 합니다.")
    private String email;

    @Column(nullable = false)
    private String companyName;

    @Column(nullable = false)
    private String companySize;

    @Column(unique = true, nullable = false)
    private String registrationNumber;

    @Column(nullable = false)
    private String industry;

    @Column(nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @Builder
    public User(Long id, String username, String password, String email, String companyName, String companySize, String registrationNumber, String industry, LocalDateTime createdAt) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.companyName = companyName;
        this.companySize = companySize;
        this.registrationNumber = registrationNumber;
        this.industry = industry;
        this.createdAt = LocalDateTime.now();
    }

}
