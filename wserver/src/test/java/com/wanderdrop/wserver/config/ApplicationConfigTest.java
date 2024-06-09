package com.wanderdrop.wserver.config;

import com.wanderdrop.wserver.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
class ApplicationConfigTest {

    @Autowired
    private ApplicationConfig applicationConfig;

    @MockBean
    private UserRepository userRepository;

    @BeforeEach
    void setup() {
        when(userRepository.findByEmail(anyString())).thenReturn(java.util.Optional.empty());
    }

    @Test
    void testUserDetailsService() {
        UserDetailsService userDetailsService = applicationConfig.userDetailsService();
        assertNotNull(userDetailsService);
    }

    @Test
    void testAuthenticationProvider() {
        AuthenticationProvider authenticationProvider = applicationConfig.authenticationProvider();
        assertNotNull(authenticationProvider);
    }

    @Test
    void testPasswordEncoder() {
        PasswordEncoder passwordEncoder = applicationConfig.passwordEncoder();
        assertNotNull(passwordEncoder);
    }

    @Test
    void testAuthenticationManager() throws Exception {
        AuthenticationManager authenticationManager = applicationConfig.authenticationManager(null);
        assertNotNull(authenticationManager);
    }
}
