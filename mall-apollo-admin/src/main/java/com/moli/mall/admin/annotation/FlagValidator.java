package com.moli.mall.admin.annotation;

import com.moli.mall.admin.annotation.constraint.FlagValidatorConstraint;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @author moli
 * @time 2024-04-03 15:55:51
 * @description 用户验证状态是否在指定范围内的注解
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy = FlagValidatorConstraint.class)
public @interface FlagValidator {

    /**
     * 允许的值
     */
    String[] value() default {};

    /**
     * 错误信息
     */
    String message() default "flag is not allowed";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
