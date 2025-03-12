package com.kobo.coworker.document.domain;

import com.kobo.coworker.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
    private Long documentId;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;

    @Column(nullable = false)
    private String documentName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FileType documentType;

    @Column(nullable = false)
    private String documentPath;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private Timestamp uploadedAt;
}
