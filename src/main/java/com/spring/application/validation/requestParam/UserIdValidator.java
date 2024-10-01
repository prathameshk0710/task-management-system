package com.spring.application.validation.requestParam;

import com.spring.application.exception.ValidationException;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.bind.MethodArgumentNotValidException;

public class UserIdValidator implements ConstraintValidator<ValidUserId, Long> {
    private String message;
    @Override
    public void initialize(ValidUserId constraintAnnotation) {
        this.message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        if(value == null){
            context.buildConstraintViolationWithTemplate(message.isEmpty() ? this.message = "Value must not be null" : message).addConstraintViolation();
            throw new ValidationException(message);
        }
        if(value <= 0) {
            context.buildConstraintViolationWithTemplate(message.isEmpty() ? this.message = "Value must be greater than 0" : message).addConstraintViolation();
            throw new ValidationException(message);
        }

        return true;
    }
}
