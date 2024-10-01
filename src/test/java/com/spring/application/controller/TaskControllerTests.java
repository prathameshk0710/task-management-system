package com.spring.application.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.application.dto.TaskDto;
import com.spring.application.enums.TaskStatus;
import com.spring.application.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ContextConfiguration(classes = {TaskController.class})
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController taskController;

    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(taskController).build();
    }

    @Test
    void testCreateTask() throws Exception {
        TaskDto taskDto = new TaskDto();
        taskDto.setTitle("New Task");

        when(taskService.createTask(any(TaskDto.class), anyLong())).thenReturn(taskDto);

        mockMvc.perform(post("/taskManager/createTask")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("userId", "1")
                        .content(objectMapper.writeValueAsString(taskDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("New Task"));
    }

    @Test
    void testGetAllTasks() throws Exception {
        TaskDto task1 = new TaskDto(); // Populate TaskDto objects
        task1.setTitle("Task 1");
        TaskDto task2 = new TaskDto();
        task2.setTitle("Task 2");

        List<TaskDto> tasks = Arrays.asList(task1, task2);

        when(taskService.getAllTasks()).thenReturn(tasks);

        mockMvc.perform(get("/taskManager/getAllTasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].title").value("Task 1"))
                .andExpect(jsonPath("$[1].title").value("Task 2"));
    }

    @Test
    void testGetTaskByTaskId() throws Exception {
        TaskDto taskDto = new TaskDto();
        taskDto.setTitle("Task by ID");

        when(taskService.getTaskById(anyLong())).thenReturn(taskDto);

        mockMvc.perform(get("/taskManager/getTaskByTaskId/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Task by ID"));
    }

    @Test
    void testGetTaskByUserId() throws Exception {
        TaskDto task1 = new TaskDto();
        task1.setTitle("Task 1");
        TaskDto task2 = new TaskDto();
        task2.setTitle("Task 2");

        List<TaskDto> tasks = Arrays.asList(task1, task2);

        when(taskService.getTaskByUserId(anyLong())).thenReturn(tasks);

        mockMvc.perform(get("/taskManager/getTaskByuserId/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].title").value("Task 1"))
                .andExpect(jsonPath("$[1].title").value("Task 2"));
    }

    @Test
    void testUpdateTask() throws Exception {
        TaskDto updatedTask = new TaskDto();
        updatedTask.setTitle("Task 1");
        updatedTask.setDescription("This is task 1");
        updatedTask.setStatus(TaskStatus.COMPLETED);

        when(taskService.updateTask(any(), any())).thenReturn(updatedTask);

        mockMvc.perform(put("/taskManager/updateTask/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedTask)))
                        .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
    }

    @Test
    void testDeleteTask() throws Exception {
        when(taskService.deleteTask(anyLong())).thenReturn("Task deleted successfully");

        mockMvc.perform(delete("/taskManager/deleteTask/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Task deleted successfully"));
    }
}
