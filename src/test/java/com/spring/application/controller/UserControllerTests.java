package com.spring.application.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.application.dto.UserDto;
import com.spring.application.service.UserService;
import com.spring.application.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ContextConfiguration(classes = {UserController.class})
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void testCreateUser() throws Exception {
        UserDto userDto = TestUtils.getUserDto1();

        when(userService.addUser(any(UserDto.class))).thenReturn(userDto);

        mockMvc.perform(post("/taskManager/createUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Bhanu"))
                .andExpect(jsonPath("$.lastName").value("Bindal"));
    }

    @Test
    void testUpdateUser() throws Exception {
        UserDto updatedUser = TestUtils.getUserDto1();

        when(userService.updateUser(anyLong(), any(UserDto.class))).thenReturn(updatedUser);

        mockMvc.perform(put("/taskManager/updateUser/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Bhanu"))
                .andExpect(jsonPath("$.lastName").value("Bindal"));
    }

    @Test
    void testDeleteUser() throws Exception {
        when(userService.deleteUser(anyLong())).thenReturn("User deleted successfully");

        mockMvc.perform(delete("/taskManager/deleteUser/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("User deleted successfully"));
    }

    @Test
    void testGetAllUsers() throws Exception {

        List<UserDto> users = Arrays.asList(TestUtils.getUserDto1(), TestUtils.getUserDto2());

        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/taskManager/getAllUsers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].firstName").value("Bhanu"))
                .andExpect(jsonPath("$[0].lastName").value("Bindal"))
                .andExpect(jsonPath("$[1].firstName").value("Prathamesh"))
                .andExpect(jsonPath("$[1].lastName").value("Kondawale"));
    }


    @Test
    void testGetUserById() throws Exception {
        UserDto userDto = TestUtils.getUserDto1();

        when(userService.getUserById(anyLong())).thenReturn(userDto);

        mockMvc.perform(get("/taskManager/getUserById/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Bhanu"))
                .andExpect(jsonPath("$.lastName").value("Bindal"));
    }
}
