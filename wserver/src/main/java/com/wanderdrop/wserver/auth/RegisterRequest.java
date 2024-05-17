package com.wanderdrop.wserver.auth;

import com.wanderdrop.wserver.model.Role;
import com.wanderdrop.wserver.model.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private Role role;
    private Status status;
}
