package com.wanderdrop.wserver.repository;

import com.wanderdrop.wserver.model.Attraction;
import com.wanderdrop.wserver.model.Role;
import com.wanderdrop.wserver.model.Status;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import com.wanderdrop.wserver.model.User;

import java.sql.Timestamp;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AttractionRepositoryTest {

    @Autowired
    private AttractionRepository attractionRepository;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        attractionRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @Transactional
    public void testSaveAttraction() {

        User user = new User();
        user.setEmail("testuser@test.com");
        user.setPassword("password");
        user.setFirstName("Firstname");
        user.setLastName("Lastname");
        user.setRole(Role.USER);
        user.setStatus(Status.ACTIVE);
        user.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        userRepository.save(user);

        Attraction attraction = new Attraction(null, "Attraction 1", "Description 1", 22.1234, 16.3545, user, user, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()), Status.ACTIVE, null);

        attractionRepository.save(attraction);

        Attraction found = attractionRepository.findById(attraction.getAttractionId()).orElse(null);
        assertThat(found).isNotNull();
        assert found != null;
        assertThat(found.getName()).isEqualTo("Attraction 1");
    }
}
