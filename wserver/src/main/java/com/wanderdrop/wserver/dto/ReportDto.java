package com.wanderdrop.wserver.dto;

import com.wanderdrop.wserver.model.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
    public class ReportDto {

    private Long reportId;
    private String reportHeading;
    private String reportMessage;
    private Long attractionId;
    private UUID createdBy;
    private LocalDateTime createdAt;
    private Status status;
}
