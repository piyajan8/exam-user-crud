package com.macode101.exam.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.macode101.exam.model.UserDto;
import com.macode101.exam.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllUsersShouldReturnAllUsers_WhenUsersExist() throws Exception {
        List<UserDto> users = Arrays.asList(
            new UserDto(1L, "John Doe", "johndoe", "john@example.com", "123-456-7890", "www.johndoe.com"),
            new UserDto(2L, "Jane Smith", "janesmith", "jane@example.com", "098-765-4321", "www.janesmith.com")
        );
        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[0].username").value("johndoe"))
                .andExpect(jsonPath("$[0].email").value("john@example.com"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Jane Smith"));
    }

    @Test
    void createUserShouldReturnCreatedUserWhenValidData() throws Exception {
        UserDto inputUser = new UserDto(null, "John Doe", "johndoe", "john@example.com", "123-456-7890", "www.johndoe.com");
        UserDto createdUser = new UserDto(1L, "John Doe", "johndoe", "john@example.com", "123-456-7890", "www.johndoe.com");
        when(userService.createUser(any(UserDto.class))).thenReturn(createdUser);

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputUser)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.username").value("johndoe"))
                .andExpect(jsonPath("$.email").value("john@example.com"));
    }

    @Test
    void createUserShouldReturn400WhenNameIsMissing() throws Exception {
        UserDto invalidUser = new UserDto(null, "", "johndoe", "john@example.com", "123-456-7890", "www.johndoe.com");

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidUser)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createUserShouldReturn400WhenUsernameIsMissing() throws Exception {
        UserDto invalidUser = new UserDto(null, "John Doe", "", "john@example.com", "123-456-7890", "www.johndoe.com");

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidUser)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createUserShouldReturn400WhenEmailIsMissing() throws Exception {
        UserDto invalidUser = new UserDto(null, "John Doe", "johndoe", "", "123-456-7890", "www.johndoe.com");

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidUser)))
                .andExpect(status().isBadRequest());
    }
}
