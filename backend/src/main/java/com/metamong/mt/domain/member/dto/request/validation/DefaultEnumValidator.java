package com.metamong.mt.domain.member.dto.request.validation;

import java.util.Arrays;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DefaultEnumValidator implements ConstraintValidator<EnumValidator, String> {

    private Class<? extends Enum<?>> enumClass;

    @Override
    public void initialize(EnumValidator constraintAnnotation) {
        this.enumClass = constraintAnnotation.enumClass();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return false;
        
        return Arrays.stream(enumClass.getEnumConstants())
                .map(Enum::name) // Enum의 name()을 가져와서 비교
                .anyMatch(enumValue -> enumValue.equals(value));
    }

}
