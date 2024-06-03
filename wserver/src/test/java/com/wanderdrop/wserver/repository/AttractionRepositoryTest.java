package com.wanderdrop.wserver.repository;

import com.wanderdrop.wserver.model.Attraction;
import com.wanderdrop.wserver.model.Role;
import com.wanderdrop.wserver.model.Status;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.wanderdrop.wserver.model.User;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class AttractionRepositoryTest {

    @Autowired
    private AttractionRepository attractionRepository;

    private User user;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setEmail("testuser@test.com");
        user.setPassword("password");
        user.setFirstName("Firstname");
        user.setLastName("Lastname");
        user.setRole(Role.USER);
        user.setStatus(Status.ACTIVE);
        user.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        userRepository.save(user);
    }


    @AfterEach
    void tearDown() {
        attractionRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void testFindByCreatedBy() {
        Attraction attraction1 = new Attraction(null, "Attraction 1", "Description 1", 22.1234, 16.3545, user, user, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()), Status.ACTIVE, null);
        Attraction attraction2 = new Attraction(null, "Attraction 2", "Description 2", 32.1234, 17.3545, user, user, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()), Status.ACTIVE, null);
        attractionRepository.save(attraction1);
        attractionRepository.save(attraction2);

        List<Attraction> attractions = attractionRepository.findByCreatedBy(user);

        assertThat(attractions).hasSize(2);
        assertThat(attractions).contains(attraction1, attraction2);
    }

    @Test
    public void testFindNonExistingAttraction() {
        Optional<Attraction> found = attractionRepository.findById(999L);
        assertThat(found).isEmpty();
    }

    @Test
    public void testFindAttractionById() {
        Attraction attraction = new Attraction(null, "Attraction 1", "Description 1", 22.1234, 16.3545, user, user, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()), Status.ACTIVE, null);
        attractionRepository.save(attraction);

        Attraction found = attractionRepository.findById(attraction.getAttractionId()).orElse(null);
        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo("Attraction 1");
    }

    @Test
    public void testGetAllAttractions() {
        Attraction attraction1 = new Attraction(null, "Attraction 1", "Description 1", 22.1234, 16.3545, user, user, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()), Status.ACTIVE, null);
        Attraction attraction2 = new Attraction(null, "Attraction 2", "Description 2", 32.1234, 17.3545, user, user, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()), Status.ACTIVE, null);
        attractionRepository.save(attraction1);
        attractionRepository.save(attraction2);

        List<Attraction> attractions = attractionRepository.findAll();

        assertThat(attractions).hasSize(2);
        assertThat(attractions).contains(attraction1, attraction2);
    }

    @Test
    public void testSaveAttraction() {

        Attraction attraction = new Attraction(null, "Attraction 1", "Description 1", 22.1234, 16.3545, user, user, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()), Status.ACTIVE, null);

        attractionRepository.save(attraction);

        Attraction found = attractionRepository.findById(attraction.getAttractionId()).orElse(null);
        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo("Attraction 1");
    }

    @Test
    public void testUpdateAttraction() {

        Attraction attraction = new Attraction(null, "Attraction 1", "Description 1", 22.1234, 16.3545, user, user, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()), Status.ACTIVE, null);
        attractionRepository.save(attraction);

        attraction.setName("Updated Attraction");
        attractionRepository.save(attraction);

        Attraction found = attractionRepository.findById(attraction.getAttractionId()).orElse(null);
        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo("Updated Attraction");
    }

    @Test
    public void testDeleteAttraction() {

        Attraction attraction = new Attraction(null, "Attraction 1", "Description 1", 22.1234, 16.3545, user, user, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()), Status.ACTIVE, null);
        attractionRepository.save(attraction);

        attractionRepository.delete(attraction);

        Optional<Attraction> found = attractionRepository.findById(attraction.getAttractionId());
        assertThat(found).isEmpty();
    }
}
