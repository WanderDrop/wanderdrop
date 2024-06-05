package com.wanderdrop.wserver.controller;

import com.wanderdrop.wserver.dto.PasswordChangeRequest;
import com.wanderdrop.wserver.dto.UpdateUserRequest;
import com.wanderdrop.wserver.model.User;
import com.wanderdrop.wserver.service.jwt.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userService;

    @PostMapping("/{userId}/change-password")
    public ResponseEntity<?> changePassword(@PathVariable UUID userId, @RequestBody PasswordChangeRequest request) {
        boolean success = userService.changePassword(userId, request.getOldPassword(), request.getNewPassword());
        if (success) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Old password is incorrect");
        }
    }

    @PutMapping("/{userId}/update")
    public ResponseEntity<?> updateUser(@PathVariable UUID userId, @RequestBody UpdateUserRequest request) {
        User updatedUser = userService.updateUser(userId, request.getFirstName(), request.getLastName());
        return ResponseEntity.ok(updatedUser);
    }
}
