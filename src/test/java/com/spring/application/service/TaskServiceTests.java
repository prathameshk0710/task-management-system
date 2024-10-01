package com.spring.application.service;

import com.spring.application.dto.TaskDto;
import com.spring.application.entity.Task;
import com.spring.application.entity.User;
import com.spring.application.enums.TaskStatus;
import com.spring.application.exception.TaskNotFoundException;
import com.spring.application.repository.ITaskRepository;
import com.spring.application.repository.IUserRepository;
import com.spring.application.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class TaskServiceTest {

    @InjectMocks
    private TaskService taskService;

    @Mock
    private ITaskRepository taskRepository;

    @Mock
    private IUserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    private TaskDto taskDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        taskDto = TestUtils.getTaskDto2();
    }

    @Test
    void testCreateTask() {
        Task task = TestUtils.getTask2();

        when(modelMapper.map(any(TaskDto.class), eq(Task.class))).thenReturn(task);
        when(taskRepository.save(any(Task.class))).thenReturn(task);
        when(modelMapper.map(any(Task.class), eq(TaskDto.class))).thenReturn(taskDto);

        TaskDto createdTask = taskService.createTask(taskDto, 1L);

        assertEquals("Task 2", createdTask.getTitle());
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void testGetAllTasks() {
        Task task1 = TestUtils.getTask1();
        Task task2 = TestUtils.getTask2();

        when(taskRepository.findAll()).thenReturn(Arrays.asList(task1, task2));
        when(modelMapper.map(any(Task.class), eq(TaskDto.class))).thenReturn(taskDto);

        List<TaskDto> tasks = taskService.getAllTasks();

        assertEquals(2, tasks.size());
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    void testGetAllTasksNoTasksFound() {
        when(taskRepository.findAll()).thenReturn(Arrays.asList());

        TaskNotFoundException exception = assertThrows(TaskNotFoundException.class, () -> {
            taskService.getAllTasks();
        });

        assertEquals("No tasks found", exception.getMessage());
    }

    @Test
    void testGetTaskById() {
        Task task = TestUtils.getTask1();

        when(taskRepository.findById(anyLong())).thenReturn(Optional.of(task));
        when(modelMapper.map(any(Task.class), eq(TaskDto.class))).thenReturn(taskDto);

        TaskDto foundTask = taskService.getTaskById(1L);

        assertEquals("Task 2", foundTask.getTitle());
        verify(taskRepository, times(1)).findById(anyLong());
    }

    @Test
    void testGetTaskByIdNotFound() {
        when(taskRepository.findById(anyLong())).thenReturn(Optional.empty());

        TaskNotFoundException exception = assertThrows(TaskNotFoundException.class, () -> {
            taskService.getTaskById(1L);
        });

        assertEquals("Task not found with id: 1", exception.getMessage());
    }

    @Test
    void testUpdateTask() {
        Task existingTask = TestUtils.getTask1();

        when(taskRepository.existsById(anyLong())).thenReturn(true);
        when(taskRepository.findById(anyLong())).thenReturn(Optional.of(existingTask));
        when(modelMapper.map(any(Task.class), eq(TaskDto.class))).thenReturn(taskDto);
        when(taskRepository.save(any(Task.class))).thenReturn(existingTask);

        TaskDto updatedTask = taskService.updateTask(1L, taskDto);

        assertEquals("Task 2", updatedTask.getTitle());
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void testUpdateTaskNotFound() {
        when(taskRepository.existsById(anyLong())).thenReturn(false);

        TaskNotFoundException exception = assertThrows(TaskNotFoundException.class, () -> {
            taskService.updateTask(1L, taskDto);
        });

        assertEquals("Task not found with id: 1", exception.getMessage());
    }

    @Test
    void testDeleteTask() {
        when(taskRepository.existsById(anyLong())).thenReturn(true);

        String response = taskService.deleteTask(1L);

        assertEquals("Task deleted successfully", response);
        verify(taskRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void testDeleteTaskNotFound() {
        when(taskRepository.existsById(anyLong())).thenReturn(false);

        TaskNotFoundException exception = assertThrows(TaskNotFoundException.class, () -> {
            taskService.deleteTask(1L);
        });

        assertEquals("Task not found with id: 1", exception.getMessage());
    }

    @Test
    void testGetTaskByUserId() {
        User user = TestUtils.getUser1();

        Task task1 = TestUtils.getTask1();

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(taskRepository.findAllByAssignedTo(anyLong())).thenReturn(Optional.of(Arrays.asList(task1)));
        when(modelMapper.map(any(Task.class), eq(TaskDto.class))).thenReturn(taskDto);

        List<TaskDto> tasks = taskService.getTaskByUserId(1L);

        assertEquals(1, tasks.size());
        assertEquals("Task 2", tasks.get(0).getTitle());
        verify(taskRepository, times(1)).findAllByAssignedTo(anyLong());
    }

    @Test
    void testGetTaskByUserIdNoTasksFound() {
        User user = TestUtils.getUser1();

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(taskRepository.findAllByAssignedTo(anyLong())).thenReturn(Optional.of(Arrays.asList()));

        TaskNotFoundException exception = assertThrows(TaskNotFoundException.class, () -> {
            taskService.getTaskByUserId(1L);
        });

        assertEquals("No tasks found for this user.", exception.getMessage());
    }
}
