package com.wanderdrop.wserver.repository;

import com.wanderdrop.wserver.model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    public void setup() {
        user = new User();
        user.setEmail("test@profile.com");
        user.setPassword("password");
        user.setFirstName("Test");
        user.setLastName("User");
        user.setRole(Role.USER);
        user.setStatus(Status.ACTIVE);
        user.setUpdatedAt(Timestamp.from(Instant.now()));
        userRepository.save(user);

    }

    @AfterEach
    public void teardown() {
        userRepository.deleteAll();
    }

    @Test
    public void testFindByEmail() {
        Optional<User> user1 = userRepository.findByEmail(user.getEmail());

        assertNotNull(user1);
        assertEquals("test@profile.com",user1.get().getEmail());
        assertEquals("Test",user1.get().getFirstName());
        assertEquals("User",user1.get().getLastName());
        assertEquals(Status.ACTIVE,user1.get().getStatus());

    }

    @Test
    public void testFindByRole() {
        List<User> user1 = userRepository.findByRole(user.getRole());

        assertNotNull(user1);
        assertEquals(1, user1.size());
        assertEquals("Test",user1.get(0).getFirstName());
        assertEquals("User",user1.get(0).getLastName());
        assertEquals(Status.ACTIVE,user1.get(0).getStatus());

    }
}
