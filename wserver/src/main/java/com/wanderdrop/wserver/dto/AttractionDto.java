package com.wanderdrop.wserver.dto;

import com.wanderdrop.wserver.model.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttractionDto {

    private Long attractionId;
    private String name;
    private String description;
    private Double latitude;
    private Double longitude;
    private Status status;
    private Long createdBy;
    private Long updatedBy;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Long deletionReasonId;
}
