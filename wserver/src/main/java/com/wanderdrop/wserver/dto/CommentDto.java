package com.wanderdrop.wserver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    private Long commentId;
    private String commentHeading;
    private String commentText;
    private String createdBy;
    private Timestamp createdAt;
    private String deletionReason;
}
