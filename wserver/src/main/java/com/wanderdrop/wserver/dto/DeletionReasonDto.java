package com.wanderdrop.wserver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeletionReasonDto {
    private Long id;
    private String reasonMessage;
}
