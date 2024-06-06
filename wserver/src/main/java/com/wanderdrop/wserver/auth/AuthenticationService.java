package com.wanderdrop.wserver.auth;

import com.wanderdrop.wserver.dto.AuthenticationRequest;
import com.wanderdrop.wserver.dto.AuthenticationResponse;
import com.wanderdrop.wserver.utils.JwtUtil;
import com.wanderdrop.wserver.model.Status;
import com.wanderdrop.wserver.model.User;
import com.wanderdrop.wserver.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
                .firstName(request.getFirstname())
                .lastName(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .status(Status.ACTIVE)
                .build();
        repository.save(user);
        var jwtToken = jwtUtil.generateToken(user);
        var response = AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
        response.userId = user.getUserId();
        response.role = user.getRole();
        return response;
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = repository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        System.out.println(user);
        var jwtToken = jwtUtil.generateToken(user);
        var response = AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
        response.userId = user.getUserId();
        response.role = user.getRole();
        response.firstName = user.getFirstName();
        response.lastName = user.getLastName();

        return response;
    }
}
