package org.internship.jpaonlinebanking.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = UserPersonalCodeNrValidator.class)
public @interface ValidPersonalCodeNr {
    String message() default "Invalid personal code number";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
