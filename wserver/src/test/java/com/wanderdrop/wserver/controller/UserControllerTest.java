package com.wanderdrop.wserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wanderdrop.wserver.config.ApplicationConfig;
import com.wanderdrop.wserver.dto.PasswordChangeRequest;
import com.wanderdrop.wserver.dto.UpdateUserRequest;
import com.wanderdrop.wserver.exeption.IncorrectPasswordException;
import com.wanderdrop.wserver.model.Role;
import com.wanderdrop.wserver.model.Status;
import com.wanderdrop.wserver.model.User;
import com.wanderdrop.wserver.repository.UserRepository;
import com.wanderdrop.wserver.service.jwt.UserServiceImpl;
import com.wanderdrop.wserver.utils.JwtUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@AutoConfigureMockMvc
@SpringBootTest
public class UserControllerTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Mock
    private UserServiceImpl userService;

    private User user;
    private String userJwtToken;

    @Autowired
    private ApplicationConfig applicationConfig;

    @BeforeEach
    public void setUp() {

        user = new User();
        user.setEmail("user@usertest.com");
        user.setPassword(applicationConfig.passwordEncoder().encode("password"));
        user.setFirstName("User");
        user.setLastName("Test");
        user.setRole(Role.USER);
        user.setStatus(Status.ACTIVE);
        user.setCreatedAt(Timestamp.from(Instant.now()));
        user = userRepository.save(user);

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        userJwtToken = "Bearer " + jwtUtil.generateToken(userDetails);

    }

    @AfterEach
    public void cleanUp() {
        userRepository.deleteAll();
    }

    @Test
    public void testChangePassword_Success() throws Exception {
        PasswordChangeRequest request = new PasswordChangeRequest();
        request.setOldPassword("password");
        request.setNewPassword("newpassword");

        when(userService.changePassword(any(UUID.class), anyString(), anyString())).thenReturn(true);

        mockMvc.perform(post("/api/users/{userId}/change-password", user.getUserId())
                        .header("Authorization", userJwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

    }

    @Test
    public void testChangePassword_IncorrectOldPassword() throws Exception {
        PasswordChangeRequest request = new PasswordChangeRequest();
        request.setOldPassword("wrongpassword");
        request.setNewPassword("newpassword");

        when(userService.changePassword(any(UUID.class), anyString(), anyString())).thenThrow(new IncorrectPasswordException("Old password is incorrect"));

        mockMvc.perform(post("/api/users/{userId}/change-password", user.getUserId())
                        .header("Authorization", userJwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden())
                .andExpect(content().string("Old password is incorrect"));

    }

    @Test
    public void testUpdateUser_Success() throws Exception {
        UpdateUserRequest request = new UpdateUserRequest();
        request.setFirstName("UpdatedFirstName");
        request.setLastName("UpdatedLastName");

        User updatedUser = new User();
        updatedUser.setUserId(user.getUserId());
        updatedUser.setEmail(user.getEmail());
        updatedUser.setFirstName("UpdatedFirstName");
        updatedUser.setLastName("UpdatedLastName");

        when(userService.updateUser(any(UUID.class), anyString(), anyString())).thenReturn(updatedUser);

        mockMvc.perform(put("/api/users/{userId}/update", user.getUserId())
                        .header("Authorization", userJwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

    }

    @Test
    public void testUpdateUser_Unauthorized() throws Exception {
        UpdateUserRequest request = new UpdateUserRequest();
        request.setFirstName("UpdatedFirstName");
        request.setLastName("UpdatedLastName");

        mockMvc.perform(put("/api/users/{userId}/update", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }
}
