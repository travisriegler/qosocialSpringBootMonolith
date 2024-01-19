package com.qosocial.v1api.profile.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Target({ ElementType.FIELD })
@Constraint(validatedBy = NoReservedWordsValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface NoReservedWords {
    String message() default "Invalid username";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

