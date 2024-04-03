package com.moli.mall.admin.annotation.constraint;

import com.moli.mall.admin.annotation.FlagValidator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

/**
 * @author moli
 * @time 2024-04-03 16:00:45
 * @description 标志校验类
 */
public class FlagValidatorConstraint implements ConstraintValidator<FlagValidator, Integer> {

    private String[] values;

    @Override
    public void initialize(FlagValidator constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        this.values = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if (Objects.isNull(value)) return false;

        for (String val : values) {
            if (val.equals(String.valueOf(value))) {
                return true;
            }
        }
        return false;
    }
}
