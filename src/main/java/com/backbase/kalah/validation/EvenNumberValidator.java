package com.backbase.kalah.validation;

import com.backbase.kalah.validation.annotation.EvenNumber;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EvenNumberValidator implements ConstraintValidator<EvenNumber, Integer> {

    @Override
    public void initialize(EvenNumber evenNumber) {
    }

    @Override
    public boolean isValid(Integer evenField, ConstraintValidatorContext cxt) {
        return evenField != null && evenField % 2 == 0;
    }
}
