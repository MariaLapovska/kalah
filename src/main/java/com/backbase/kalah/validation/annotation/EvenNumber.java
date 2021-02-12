package com.backbase.kalah.validation.annotation;

import com.backbase.kalah.validation.EvenNumberValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EvenNumberValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
public @interface EvenNumber {
    String message() default "must be an even number";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
