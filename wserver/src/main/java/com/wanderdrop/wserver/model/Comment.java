package com.wanderdrop.wserver.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.security.Timestamp;
import java.util.Date;

@Entity
@Data
@Table(name = "comment")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private int commentId;

    @Column(name = "comment_heading", nullable = false)
    private String commentHeading;

    @Column(name = "comment_text", nullable = false)
    private String commentText;

    @Column(name = "status", nullable = false, columnDefinition = "ENUM('active', 'deleted') DEFAULT 'active'")
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", referencedColumnName = "user_id", nullable = false, updatable = false)
    private User createdBy;

    @Column(name = "created_at", nullable = false, insertable = false, updatable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Date createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deletion_reason_id", referencedColumnName = "deletion_reason_id")
    private DeletionReason deletionReason;
}
