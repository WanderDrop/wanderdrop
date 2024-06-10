package com.wanderdrop.wserver.dto;

import com.wanderdrop.wserver.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    private String token;
    public Role role;
    public UUID userId;
    public String firstName;
    public String lastName;
    public String email;
    public LocalDateTime createdAt;
}
