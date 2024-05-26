package com.wanderdrop.wserver.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.wanderdrop.wserver.dto.AttractionDto;
import com.wanderdrop.wserver.model.Attraction;
import com.wanderdrop.wserver.model.Role;
import com.wanderdrop.wserver.model.Status;
import com.wanderdrop.wserver.model.User;
import com.wanderdrop.wserver.repository.AttractionRepository;
import com.wanderdrop.wserver.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.Timestamp;
import java.util.*;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@AutoConfigureMockMvc
@SpringBootTest
class AttractionControllerTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AttractionRepository attractionRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        attractionRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void getAllAttractions() throws Exception {

        var response = mockMvc.perform(get("/api/attractions")).andReturn();
        var attractions = objectMapper.readValue(response.getResponse().getContentAsString(), new TypeReference<List<AttractionDto>>() {
        });

        assertEquals(new ArrayList<Attraction>(), attractions);

    }

    @Test
    void testGetAllAttractionsWithUserAndAttractions() throws Exception {

        user = new User();
        user.setEmail("testuser@test.com");
        user.setPassword("password");
        user.setFirstName("Firstname");
        user.setLastName("Lastname");
        user.setRole(Role.USER);
        user.setStatus(Status.ACTIVE);
        user.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        userRepository.save(user);
        Attraction attraction1 = new Attraction(null, "Attraction 1", "Description 1", 22.1234, 16.3545, user, user, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()), Status.ACTIVE, null);
        Attraction attraction2 = new Attraction(null, "Attraction 2", "Description 2", 32.1234, 17.3545, user, user, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()), Status.ACTIVE, null);
        attractionRepository.save(attraction1);
        attractionRepository.save(attraction2);

        var response = mockMvc.perform(get("/api/attractions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andReturn();

        var attractions = objectMapper.readValue(response.getResponse().getContentAsString(), new TypeReference<List<AttractionDto>>() {
        });
        assertEquals(2, attractions.size());
    }

}