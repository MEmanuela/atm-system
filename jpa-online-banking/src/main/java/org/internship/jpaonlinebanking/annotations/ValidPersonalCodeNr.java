package org.internship.jpaonlinebanking.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = UserPersonalCodeNrValidator.class)
public @interface ValidPersonalCodeNr {
    String message() default "Invalid personal code number";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
