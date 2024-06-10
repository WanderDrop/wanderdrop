package com.wanderdrop.wserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wanderdrop.wserver.dto.CommentDto;
import com.wanderdrop.wserver.model.*;
import com.wanderdrop.wserver.repository.AttractionRepository;
import com.wanderdrop.wserver.repository.CommentRepository;
import com.wanderdrop.wserver.repository.UserRepository;
import com.wanderdrop.wserver.utils.JwtUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AttractionRepository attractionRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    private User user;
    private User admin;
    private Attraction attraction;
    private String userJwtToken;
    private String adminJwtToken;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setFirstName("Test");
        user.setLastName("User");
        user.setRole(Role.USER);
        user.setStatus(Status.ACTIVE);
        user.setCreatedAt(LocalDateTime.now());
        user = userRepository.save(user);

        admin = new User();
        admin.setEmail("admin@example.com");
        admin.setPassword("password");
        admin.setFirstName("Admin");
        admin.setLastName("User");
        admin.setRole(Role.ADMIN);
        admin.setStatus(Status.ACTIVE);
        admin.setCreatedAt(LocalDateTime.now());
        admin = userRepository.save(admin);

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        userJwtToken = "Bearer " + jwtUtil.generateToken(userDetails);

        UserDetails adminDetails = userDetailsService.loadUserByUsername(admin.getEmail());
        adminJwtToken = "Bearer " + jwtUtil.generateToken(adminDetails);

        attraction = new Attraction();
        attraction.setName("Test Attraction");
        attraction.setDescription("This is a test description.");
        attraction.setLatitude(40.7128);
        attraction.setLongitude(-74.0060);
        attraction.setCreatedBy(user);
        attraction.setCreatedAt(LocalDateTime.now());
        attraction.setStatus(Status.ACTIVE);
        attraction = attractionRepository.save(attraction);
    }

    @AfterEach
    public void cleanUp() {
        commentRepository.deleteAll();
        attractionRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void testGetAllActiveComments() throws Exception {
        Comment comment = new Comment();
        comment.setCommentHeading("Test Heading");
        comment.setCommentText("Test Text");
        comment.setStatus(Status.ACTIVE);
        comment.setAttraction(attraction);
        comment.setCreatedBy(user);
        comment.setCreatedAt(LocalDateTime.now());
        comment = commentRepository.save(comment);

        mockMvc.perform(get("/api/comments/attraction/{attractionId}", attraction.getAttractionId())
                        .header("Authorization", userJwtToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].commentHeading").value("Test Heading"))
                .andExpect(jsonPath("$[0].commentText").value("Test Text"));

        commentRepository.delete(comment);
    }

    @Test
    public void testGetCommentById() throws Exception {
        Comment comment = new Comment();
        comment.setCommentHeading("Test Heading");
        comment.setCommentText("Test Text");
        comment.setStatus(Status.ACTIVE);
        comment.setAttraction(attraction);
        comment.setCreatedBy(user);
        comment.setCreatedAt(LocalDateTime.now());
        comment = commentRepository.save(comment);

        mockMvc.perform(get("/api/comments/{id}", comment.getCommentId())
                        .header("Authorization", userJwtToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.commentHeading").value("Test Heading"))
                .andExpect(jsonPath("$.commentText").value("Test Text"));

        commentRepository.delete(comment);
    }

    @Test

    public void testCreateComment() throws Exception {
        CommentDto commentDto = new CommentDto();
        commentDto.setCommentHeading("New Heading");
        commentDto.setCommentText("New Text");

        mockMvc.perform(post("/api/comments/{attractionId}", attraction.getAttractionId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentDto))
                        .header("Authorization", userJwtToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.commentHeading").value("New Heading"))
                .andExpect(jsonPath("$.commentText").value("New Text"));

        List<Comment> comments = commentRepository.findAll();
        assertEquals(1, comments.size());
        assertEquals("New Heading", comments.get(0).getCommentHeading());
        assertEquals("New Text", comments.get(0).getCommentText());

        commentRepository.deleteAll();
    }

    @Test
    public void testUpdateComment() throws Exception {
        Comment comment = new Comment();
        comment.setCommentHeading("Old Heading");
        comment.setCommentText("Old Text");
        comment.setStatus(Status.ACTIVE);
        comment.setAttraction(attraction);
        comment.setCreatedBy(user);
        comment.setCreatedAt(LocalDateTime.now());
        comment = commentRepository.save(comment);

        CommentDto updatedCommentDto = new CommentDto();
        updatedCommentDto.setCommentHeading("Updated Heading");
        updatedCommentDto.setCommentText("Updated Text");

        mockMvc.perform(put("/api/comments/{id}", comment.getCommentId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedCommentDto))
                        .header("Authorization", adminJwtToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.commentHeading").value("Updated Heading"))
                .andExpect(jsonPath("$.commentText").value("Updated Text"));

        Comment updatedComment = commentRepository.findById(comment.getCommentId()).orElse(null);
        assertNotNull(updatedComment);
        assertEquals("Updated Heading", updatedComment.getCommentHeading());
        assertEquals("Updated Text", updatedComment.getCommentText());

        commentRepository.delete(updatedComment);
    }

    @Test
    public void testUpdateComment_AsUser() throws Exception {
        Comment comment = new Comment();
        comment.setCommentHeading("Old Heading");
        comment.setCommentText("Old Text");
        comment.setStatus(Status.ACTIVE);
        comment.setAttraction(attraction);
        comment.setCreatedBy(user);
        comment.setCreatedAt(LocalDateTime.now());
        comment = commentRepository.save(comment);

        CommentDto updatedCommentDto = new CommentDto();
        updatedCommentDto.setCommentHeading("Updated Heading");
        updatedCommentDto.setCommentText("Updated Text");

        mockMvc.perform(put("/api/comments/{id}", comment.getCommentId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedCommentDto))
                        .header("Authorization", userJwtToken))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testDeleteComment_AsUser() throws Exception {
        Comment comment = new Comment();
        comment.setCommentHeading("Heading to Delete");
        comment.setCommentText("Text to Delete");
        comment.setStatus(Status.ACTIVE);
        comment.setAttraction(attraction);
        comment.setCreatedBy(user);
        comment.setCreatedAt(LocalDateTime.now());
        comment = commentRepository.save(comment);

        mockMvc.perform(put("/api/comments/{id}/{reasonId}", comment.getCommentId(), 1L)
                        .header("Authorization", userJwtToken))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testDeleteComment() throws Exception {
        Comment comment = new Comment();
        comment.setCommentHeading("Heading to Delete");
        comment.setCommentText("Text to Delete");
        comment.setStatus(Status.ACTIVE);
        comment.setAttraction(attraction);
        comment.setCreatedBy(user);
        comment.setCreatedAt(LocalDateTime.now());
        comment = commentRepository.save(comment);

        mockMvc.perform(put("/api/comments/{id}/{reasonId}", comment.getCommentId(), 1L).header("Authorization", adminJwtToken))
                .andExpect(status().isOk());

        Comment deletedComment = commentRepository.findById(comment.getCommentId()).orElse(null);
        assertNotNull(deletedComment);
        assertEquals(Status.DELETED, deletedComment.getStatus());

        commentRepository.delete(deletedComment);
    }

    @Test
    public void testCreateComment_Unauthorized() throws Exception {
        CommentDto commentDto = new CommentDto();
        commentDto.setCommentHeading("Unauthorized Heading");
        commentDto.setCommentText("Unauthorized Text");

        mockMvc.perform(post("/api/comments/{attractionId}", 999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentDto)))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testGetAllActiveComments_Unauthorized() throws Exception {
        mockMvc.perform(get("/api/comments/attraction/{attractionId}", attraction.getAttractionId()))
                .andExpect(status().isOk());
    }

    @Test
    public void testCreateComment_Unauthenticated() throws Exception {
        CommentDto commentDto = new CommentDto();
        commentDto.setCommentHeading("Unauthorized Heading");
        commentDto.setCommentText("Unauthorized Text");

        mockMvc.perform(post("/api/comments/{attractionId}", attraction.getAttractionId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentDto)))
                .andExpect(status().isForbidden());
    }
}