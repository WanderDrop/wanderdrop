package com.wanderdrop.wserver.service.jwt;

import com.wanderdrop.wserver.dto.PasswordChangeRequest;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.UUID;

public interface UserService{
    UserDetailsService userDetailsService();
    boolean changePassword(UUID userId, String oldPassword, String newPassword);
}