package com.wanderdrop.wserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wanderdrop.wserver.dto.DeletionReasonDto;
import com.wanderdrop.wserver.model.DeletionReason;
import com.wanderdrop.wserver.repository.DeletionReasonRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@AutoConfigureMockMvc
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DeletionReasonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DeletionReasonRepository deletionReasonRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @AfterAll
    public void cleanUp() {
        deletionReasonRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetDefaultDeletionReasons() throws Exception {
        List<DeletionReasonDto> defaultReasons = deletionReasonRepository.findAll().stream()
                .filter(DeletionReason::isDefaultReason)
                .map(deletionReason -> new DeletionReasonDto(deletionReason.getDeletionReasonId(), deletionReason.getReasonMessage()))
                .collect(Collectors.toList());

        mockMvc.perform(get("/api/deletion-reasons"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(defaultReasons.size())))
                .andExpect(content().json(objectMapper.writeValueAsString(defaultReasons)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testSaveDeletionReason() throws Exception {
        DeletionReasonDto newReasonDto = new DeletionReasonDto(null, "New Custom Reason");

        mockMvc.perform(post("/api/deletion-reasons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newReasonDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.reasonMessage").value("New Custom Reason"));

        assertEquals(8, deletionReasonRepository.count());
    }

    @Test
    public void testGetDefaultDeletionReasons_Unauthorized() throws Exception {
        mockMvc.perform(get("/api/deletion-reasons"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testSaveDeletionReason_Unauthorized() throws Exception {
        DeletionReasonDto newReasonDto = new DeletionReasonDto(null, "New Custom Reason");

        mockMvc.perform(post("/api/deletion-reasons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newReasonDto)))
                .andExpect(status().isForbidden());
    }
}