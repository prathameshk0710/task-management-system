package com.spring.application.validation.taskStatus;

import com.spring.application.enums.TaskStatus;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TaskStatusValidator implements ConstraintValidator<ValidTaskStatus, TaskStatus> {

    @Override
    public void initialize(ValidTaskStatus constraintAnnotation) {
    }

    @Override
    public boolean isValid(TaskStatus status, ConstraintValidatorContext context) {
        if (status == null) {
            return true;
        }
        return status == TaskStatus.IN_PROGRESS || status == TaskStatus.COMPLETED || status == TaskStatus.PENDING;
    }
}
