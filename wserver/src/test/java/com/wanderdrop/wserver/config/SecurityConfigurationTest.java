package com.wanderdrop.wserver.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityConfigurationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testAccessToProtectedEndpoint() throws Exception {
        mockMvc.perform(post("/api/attractions"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testAccessToNotProtectedEndpoint() throws Exception {
        mockMvc.perform(get("/api/comments/attraction/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void testAccessToProtectedPostEndpoint() throws Exception {
        mockMvc.perform(post("/api/comments/1"))
                .andExpect(status().isForbidden());
    }
}