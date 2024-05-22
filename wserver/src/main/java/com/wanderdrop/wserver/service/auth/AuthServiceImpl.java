package com.wanderdrop.wserver.service.auth;

import com.wanderdrop.wserver.model.Role;
import com.wanderdrop.wserver.model.Status;
import com.wanderdrop.wserver.model.User;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.wanderdrop.wserver.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

    private final UserRepository userRepository;

    @PostConstruct
    public void createAdminAccount(){
        User adminAccount = userRepository.findByRole(Role.ADMIN);
        if (adminAccount == null){
            User newAdminAccount = new User();
            newAdminAccount.setFirstName("Admin");
            newAdminAccount.setLastName("Account");
            newAdminAccount.setEmail("admin@test.com");
            newAdminAccount.setPassword(new BCryptPasswordEncoder().encode("admin"));
            newAdminAccount.setRole(Role.ADMIN);
            newAdminAccount.setStatus(Status.ACTIVE);
            userRepository.save(newAdminAccount);
            System.out.println("Admin account created successfully");
        }
    }

}
