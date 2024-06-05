package com.wanderdrop.wserver.service.jwt;

import com.wanderdrop.wserver.dto.PasswordChangeRequest;
import com.wanderdrop.wserver.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.UUID;

public interface UserService{
    UserDetailsService userDetailsService();
    boolean changePassword(UUID userId, String oldPassword, String newPassword);
    User updateUser(UUID userId, String firstName, String lastName);
}