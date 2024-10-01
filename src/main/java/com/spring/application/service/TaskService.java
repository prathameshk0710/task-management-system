package com.spring.application.service;

import com.spring.application.dto.TaskDto;
import com.spring.application.entity.Task;
import com.spring.application.entity.User;
import com.spring.application.enums.TaskStatus;
import com.spring.application.exception.TaskNotFoundException;
import com.spring.application.repository.ITaskRepository;
import com.spring.application.repository.IUserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private ITaskRepository taskRepository;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    ModelMapper modelMapper;

    public TaskDto createTask(TaskDto taskDto, Long assignedUser) {
        Task task = modelMapper.map(taskDto, Task.class);

        if(task.getStatus() == null)
            task.setStatus(TaskStatus.PENDING);
        task.setCreatedAt(LocalDateTime.now(ZoneOffset.UTC));
        task.setUpdatedAt(LocalDateTime.now(ZoneOffset.UTC));
        task.setAssignedTo(assignedUser);

        return modelMapper.map(taskRepository.save(task), TaskDto.class);
    }
    public List<TaskDto> getAllTasks() {
        List<TaskDto> tasks = new ArrayList<TaskDto>();
        List<Task> allTask = taskRepository.findAll();

        for(Task task : allTask)
            tasks.add(modelMapper.map(task, TaskDto.class));

        if(tasks.isEmpty())
            throw new TaskNotFoundException("No tasks found");
        return tasks;
    }
    public TaskDto getTaskById(Long id) {
        Optional<Task> task = taskRepository.findById(id);
        if(task.isPresent())
            return modelMapper.map(task.get(), TaskDto.class);
        throw new TaskNotFoundException("Task not found with id: " + id);
    }
    public TaskDto updateTask(Long taskId, TaskDto updatedTask){
        if(taskRepository.existsById(taskId)){
            Task task = taskRepository.findById(taskId).get();
            task.setTitle(updatedTask.getTitle());
            task.setDescription(updatedTask.getDescription());
            task.setStatus(updatedTask.getStatus());
            task.setUpdatedAt(LocalDateTime.now(ZoneOffset.UTC));
            return modelMapper.map(taskRepository.save(task), TaskDto.class);
        }
        throw new TaskNotFoundException("Task not found with id: " + taskId);
    }
    public String deleteTask(Long id) {
        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
            return "Task deleted successfully";
        }
        throw new TaskNotFoundException("Task not found with id: " + id);
    }
    public List<TaskDto> getTaskByUserId(long userId){
        Optional<User> user = userRepository.findById(userId);
        List<TaskDto> tasks = new ArrayList<TaskDto>();
        if(user.isPresent()){
            Optional<List<Task>> allOptionalTask = taskRepository.findAllByAssignedTo(userId);
            if(allOptionalTask.isPresent()){
                List<Task> allTask = allOptionalTask.get();
                for(Task task : allTask){
                    tasks.add(modelMapper.map(task, TaskDto.class));
                }
            }
        }
        if(tasks.isEmpty()){
            throw new TaskNotFoundException("No tasks found for this user.");
        }
        return tasks;
    }
}
