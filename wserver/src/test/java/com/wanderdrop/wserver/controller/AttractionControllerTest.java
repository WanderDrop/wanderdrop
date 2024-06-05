package com.wanderdrop.wserver.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.jayway.jsonpath.JsonPath;
import com.wanderdrop.wserver.dto.AttractionDto;
import com.wanderdrop.wserver.dto.AuthenticationRequest;
import com.wanderdrop.wserver.model.Attraction;
import com.wanderdrop.wserver.model.Role;
import com.wanderdrop.wserver.model.Status;
import com.wanderdrop.wserver.model.User;
import com.wanderdrop.wserver.repository.AttractionRepository;
import com.wanderdrop.wserver.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.sql.Timestamp;
import java.util.*;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    @AfterEach
    void tearDown() {
        attractionRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void testGetAllAttractions() throws Exception {

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

    @Test
    void testGetUserAttractions() throws Exception {
        user = new User();
        user.setEmail("testuser@test.com");
        user.setPassword(new BCryptPasswordEncoder().encode("password"));
        user.setFirstName("Firstname");
        user.setLastName("Lastname");
        user.setRole(Role.USER);
        user.setStatus(Status.ACTIVE);
        user.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        userRepository.save(user);

        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setEmail("testuser@test.com");
        authenticationRequest.setPassword("password");

        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authenticationRequest)))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        String token = JsonPath.parse(response).read("$.token");

        Attraction attraction1 = new Attraction(null, "Attraction 1", "Description 1", 22.1234, 16.3545, user, user, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()), Status.ACTIVE, null);
        Attraction attraction2 = new Attraction(null, "Attraction 2", "Description 2", 32.1234, 17.3545, user, user, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()), Status.ACTIVE, null);
        attractionRepository.save(attraction1);
        attractionRepository.save(attraction2);

        mockMvc.perform(get("/api/attractions/user")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void testGetUserAttractionsNoAttractions() throws Exception {
        user = new User();
        user.setEmail("testuser@test.com");
        user.setPassword(new BCryptPasswordEncoder().encode("password"));
        user.setFirstName("Firstname");
        user.setLastName("Lastname");
        user.setRole(Role.USER);
        user.setStatus(Status.ACTIVE);
        user.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        userRepository.save(user);

        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setEmail("testuser@test.com");
        authenticationRequest.setPassword("password");

        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authenticationRequest)))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        String token = JsonPath.parse(response).read("$.token");

        mockMvc.perform(get("/api/attractions/user")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void testGetUserAttractionsUnauthorized() throws Exception {
        mockMvc.perform(get("/api/attractions/user"))
                .andExpect(status().isForbidden());
    }

    @Test
    void testGetAttractionById() throws Exception {

        user = new User();
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

        mockMvc.perform(MockMvcRequestBuilders.get("/api/attractions/{id}", attraction.getAttractionId()))
                .andExpect(status().isOk());

    }

    @Test
    void testUserCreateAttraction() throws Exception {

        User user = new User();
        user.setEmail("testuser@test.com");
        user.setPassword(new BCryptPasswordEncoder().encode("password"));
        user.setFirstName("Firstname");
        user.setLastName("Lastname");
        user.setRole(Role.USER);
        user.setStatus(Status.ACTIVE);
        user.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        userRepository.save(user);

        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setEmail("testuser@test.com");
        authenticationRequest.setPassword("password");

        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authenticationRequest)))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        String token = JsonPath.parse(response).read("$.token");

        AttractionDto attractionDto = new AttractionDto();
        attractionDto.setName("New Attraction");
        attractionDto.setDescription("New Description");
        attractionDto.setLatitude(22.1234);
        attractionDto.setLongitude(16.3545);

        mockMvc.perform(post("/api/attractions")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(attractionDto)))
                .andExpect(status().isOk());
    }

    @Test
    void testAdminCreateAttraction() throws Exception {

        User user = new User();
        user.setEmail("testuser@test.com");
        user.setPassword(new BCryptPasswordEncoder().encode("password"));
        user.setFirstName("Firstname");
        user.setLastName("Lastname");
        user.setRole(Role.ADMIN);
        user.setStatus(Status.ACTIVE);
        user.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        userRepository.save(user);

        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setEmail("testuser@test.com");
        authenticationRequest.setPassword("password");

        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authenticationRequest)))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        String token = JsonPath.parse(response).read("$.token");

        AttractionDto attractionDto = new AttractionDto();
        attractionDto.setName("New Attraction");
        attractionDto.setDescription("New Description");
        attractionDto.setLatitude(22.1234);
        attractionDto.setLongitude(16.3545);

        mockMvc.perform(post("/api/attractions")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(attractionDto)))
                .andExpect(status().isOk());
    }

    @Test
    void testUserCreateAttractionUnauthorized() throws Exception {

        User user = new User();
        user.setEmail("testuser@test.com");
        user.setPassword(new BCryptPasswordEncoder().encode("password"));
        user.setFirstName("Firstname");
        user.setLastName("Lastname");
        user.setRole(Role.USER);
        user.setStatus(Status.ACTIVE);
        user.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        userRepository.save(user);

        AttractionDto attractionDto = new AttractionDto();
        attractionDto.setDescription("New Description");
        attractionDto.setLatitude(22.1234);
        attractionDto.setLongitude(16.3545);

        mockMvc.perform(post("/api/attractions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(attractionDto)))
                .andExpect(status().isForbidden());

        mockMvc.perform(post("/api/attractions")
                        .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJpbnZhbGlkIn0.invalidsignature")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(attractionDto)))
                .andExpect(status().isForbidden());
    }

    @Test
    void testAdminUpdateAttraction() throws Exception {

        User user = new User();
        user.setEmail("testuser@test.com");
        user.setPassword(new BCryptPasswordEncoder().encode("password"));
        user.setFirstName("Firstname");
        user.setLastName("Lastname");
        user.setRole(Role.ADMIN);
        user.setStatus(Status.ACTIVE);
        user.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        userRepository.save(user);

        Attraction attraction = new Attraction(null, "Attraction 1", "Description 1", 22.1234, 16.3545, user, user, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()), Status.ACTIVE, null);
        attractionRepository.save(attraction);

        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setEmail("testuser@test.com");
        authenticationRequest.setPassword("password");

        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authenticationRequest)))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        String token = JsonPath.parse(response).read("$.token");

        AttractionDto updatedAttractionDto = new AttractionDto();
        updatedAttractionDto.setName("Updated Attraction");
        updatedAttractionDto.setDescription("Updated Description");
        updatedAttractionDto.setLatitude(33.1234);
        updatedAttractionDto.setLongitude(17.3545);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/attractions/" + attraction.getAttractionId())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedAttractionDto)))
                .andExpect(status().isOk());
    }

    @Test
    void testUserUpdateAttractionShouldFail() throws Exception {

        User user = new User();
        user.setEmail("testuser@test.com");
        user.setPassword(new BCryptPasswordEncoder().encode("password"));
        user.setFirstName("Firstname");
        user.setLastName("Lastname");
        user.setRole(Role.USER);
        user.setStatus(Status.ACTIVE);
        user.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        userRepository.save(user);

        Attraction attraction = new Attraction(null, "Attraction 1", "Description 1", 22.1234, 16.3545, user, user, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()), Status.ACTIVE, null);
        attractionRepository.save(attraction);

        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setEmail("testuser@test.com");
        authenticationRequest.setPassword("password");

        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authenticationRequest)))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        String token = JsonPath.parse(response).read("$.token");

        AttractionDto updatedAttractionDto = new AttractionDto();
        updatedAttractionDto.setName("Updated Attraction");
        updatedAttractionDto.setDescription("Updated Description");
        updatedAttractionDto.setLatitude(33.1234);
        updatedAttractionDto.setLongitude(17.3545);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/attractions/" + attraction.getAttractionId())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedAttractionDto)))
                .andExpect(status().isForbidden());
    }

    @Test
    void testUpdateAttractionUnauthorizedShouldFail() throws Exception {

        User user = new User();
        user.setEmail("testuser@test.com");
        user.setPassword(new BCryptPasswordEncoder().encode("password"));
        user.setFirstName("Firstname");
        user.setLastName("Lastname");
        user.setRole(Role.ADMIN);
        user.setStatus(Status.ACTIVE);
        user.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        userRepository.save(user);

        Attraction attraction = new Attraction(null, "Attraction 1", "Description 1", 22.1234, 16.3545, user, user, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()), Status.ACTIVE, null);
        attractionRepository.save(attraction);

        AttractionDto updatedAttractionDto = new AttractionDto();
        updatedAttractionDto.setName("Updated Attraction");
        updatedAttractionDto.setDescription("Updated Description");
        updatedAttractionDto.setLatitude(33.1234);
        updatedAttractionDto.setLongitude(17.3545);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/attractions/" + attraction.getAttractionId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedAttractionDto)))
                .andExpect(status().isForbidden());
    }

    @Test
    void testAdminDeleteAttraction() throws Exception {

        User user = new User();
        user.setEmail("testuser@test.com");
        user.setPassword(new BCryptPasswordEncoder().encode("password"));
        user.setFirstName("Firstname");
        user.setLastName("Lastname");
        user.setRole(Role.ADMIN);
        user.setStatus(Status.ACTIVE);
        user.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        userRepository.save(user);

        Attraction attraction = new Attraction(null, "Attraction 1", "Description 1", 22.1234, 16.3545, user, user, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()), Status.ACTIVE, null);
        attractionRepository.save(attraction);

        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setEmail("testuser@test.com");
        authenticationRequest.setPassword("password");

        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authenticationRequest)))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        String token = JsonPath.parse(response).read("$.token");

        mockMvc.perform(MockMvcRequestBuilders.put("/api/attractions/" + attraction.getAttractionId() + "/1")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    void testUserDeleteAttractionShouldFail() throws Exception {

        User user = new User();
        user.setEmail("testuser@test.com");
        user.setPassword(new BCryptPasswordEncoder().encode("password"));
        user.setFirstName("Firstname");
        user.setLastName("Lastname");
        user.setRole(Role.USER);
        user.setStatus(Status.ACTIVE);
        user.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        userRepository.save(user);

        Attraction attraction = new Attraction(null, "Attraction 1", "Description 1", 22.1234, 16.3545, user, user, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()), Status.ACTIVE, null);
        attractionRepository.save(attraction);

        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setEmail("testuser@test.com");
        authenticationRequest.setPassword("password");

        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authenticationRequest)))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        String token = JsonPath.parse(response).read("$.token");

        mockMvc.perform(MockMvcRequestBuilders.put("/api/attractions/" + attraction.getAttractionId() + "/1")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    @Test
    void testDeleteAttractionUnauthorizedShouldFail() throws Exception {

        User user = new User();
        user.setEmail("testuser@test.com");
        user.setPassword(new BCryptPasswordEncoder().encode("password"));
        user.setFirstName("Firstname");
        user.setLastName("Lastname");
        user.setRole(Role.ADMIN);
        user.setStatus(Status.ACTIVE);
        user.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        userRepository.save(user);

        Attraction attraction = new Attraction(null, "Attraction 1", "Description 1", 22.1234, 16.3545, user, user, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()), Status.ACTIVE, null);
        attractionRepository.save(attraction);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/attractions/" + attraction.getAttractionId() + "/1"))
                .andExpect(status().isForbidden());
    }
}