package com.spring.application.dto;

import com.spring.application.enums.TaskStatus;
import com.spring.application.validation.taskStatus.ValidTaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskDto {

    @NotBlank(message = "Title is mandatory")
    private String title;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    @ValidTaskStatus
    private TaskStatus status;
}
