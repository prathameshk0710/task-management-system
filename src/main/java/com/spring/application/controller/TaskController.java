package com.spring.application.controller;


import com.spring.application.dto.TaskDto;
import com.spring.application.entity.Task;
import com.spring.application.service.TaskService;
import com.spring.application.validation.requestParam.ValidUserId;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/taskManager")
@Validated
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping("/createTask")
    public ResponseEntity<TaskDto> createTask(@Valid @RequestBody TaskDto taskDto, @ValidUserId @RequestParam Long userId) {
        return ResponseEntity.ok(taskService.createTask(taskDto, userId));
    }

    @GetMapping("/getAllTasks")
    public ResponseEntity<List<TaskDto>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @GetMapping("/getTaskByTaskId/{taskId}")
    public ResponseEntity<TaskDto> getTaskByTaskId(@PathVariable Long taskId) {
        return ResponseEntity.ok(taskService.getTaskById(taskId));
    }

    @GetMapping("/getTaskByuserId/{userId}")
    public ResponseEntity<List<TaskDto>> getTaskByUserId(@ValidUserId @PathVariable Long userId) {
        return ResponseEntity.ok(taskService.getTaskByUserId(userId));
    }

    @PutMapping("/updateTask/{id}")
    public ResponseEntity<TaskDto> updateTask(@PathVariable Long id, @RequestBody TaskDto taskDto) {
        return ResponseEntity.ok(taskService.updateTask(id, taskDto));
    }

    @DeleteMapping("/deleteTask/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.deleteTask(id));
    }
}
