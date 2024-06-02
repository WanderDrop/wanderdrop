package com.wanderdrop.wserver.dto;

import com.wanderdrop.wserver.model.Role;
import lombok.Data;

import java.util.UUID;

@Data
public class UserDto {

    private Long userId;
    private String email;
    private Role role;
}
