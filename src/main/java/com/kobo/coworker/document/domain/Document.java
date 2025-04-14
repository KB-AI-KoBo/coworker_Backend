package com.kobo.coworker.document.domain;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String originalFilename;

    @Column(nullable = false)
    private String fileUrl;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private Timestamp uploadedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FileType fileType;

}
