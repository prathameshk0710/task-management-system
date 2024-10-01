package com.spring.application.utils;

import com.spring.application.dto.TaskDto;
import com.spring.application.dto.UserDto;
import com.spring.application.entity.Task;
import com.spring.application.entity.User;
import com.spring.application.enums.TaskStatus;

public class TestUtils {
    public static UserDto getUserDto1(){
        UserDto userDto = new UserDto();
        userDto.setFirstName("Bhanu");
        userDto.setLastName("Bindal");
        userDto.setTimezone("UTC");
        userDto.setIsActive(true);
        return userDto;
    }

    public static UserDto getUserDto2(){
        UserDto userDto = new UserDto();
        userDto.setFirstName("Prathamesh");
        userDto.setLastName("Kondawale");
        userDto.setTimezone("UTC");
        userDto.setIsActive(true);
        return userDto;
    }

    public static TaskDto getTaskDto1(){
        TaskDto taskDto = new TaskDto();
        taskDto.setTitle("Task 1");
        taskDto.setDescription("Task 1 description");
        taskDto.setStatus(TaskStatus.COMPLETED);
        return taskDto;
    }

    public static TaskDto getTaskDto2(){
        TaskDto taskDto = new TaskDto();
        taskDto.setTitle("Task 2");
        taskDto.setDescription("Task 2 description");
        taskDto.setStatus(TaskStatus.PENDING);
        return taskDto;
    }

    public static TaskDto getTaskDto3(){
        TaskDto taskDto = new TaskDto();
        taskDto.setTitle("Task 3");
        taskDto.setDescription("Task 3 description");
        taskDto.setStatus(TaskStatus.IN_PROGRESS);
        return taskDto;
    }

    public static Task getTask1(){
        Task task = new Task();
        task.setTaskId(1L);
        task.setTitle("Task 1");
        task.setDescription("Task 1 description");
        task.setStatus(TaskStatus.COMPLETED);
        return task;
    }

    public static Task getTask2(){
        Task task = new Task();
        task.setTaskId(2L);
        task.setTitle("Task 2");
        task.setDescription("Task 2 description");
        task.setStatus(TaskStatus.PENDING);
        return task;
    }

    public static Task getTask3(){
        Task task = new Task();
        task.setTaskId(3L);
        task.setTitle("Task 3");
        task.setDescription("Task 3 description");
        task.setStatus(TaskStatus.IN_PROGRESS);
        return task;
    }

    public static User getUser1(){
        User user = new User();
        user.setUserId(1L);
        user.setFirstName("Bhanu");
        user.setLastName("Bindal");
        user.setTimezone("UTC");
        user.setIsActive(true);
        return user;
    }

    public static User getUser2(){
        User user = new User();
        user.setUserId(2L);
        user.setFirstName("Prathamesh");
        user.setLastName("Kondawale");
        user.setTimezone("UTC");
        user.setIsActive(true);
        return user;
    }
}
