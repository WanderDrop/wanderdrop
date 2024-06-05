package com.wanderdrop.wserver.dto;

import com.wanderdrop.wserver.model.Status;
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
    private Long deletionReason;
    private Long attractionId;
    private Status status;
}
