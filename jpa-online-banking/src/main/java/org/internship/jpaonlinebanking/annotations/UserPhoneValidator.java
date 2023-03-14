package org.internship.jpaonlinebanking.annotations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UserPhoneValidator implements ConstraintValidator<UserPhoneConstraint, String> {

    @Override
    public boolean isValid(String phoneField, ConstraintValidatorContext context) {
        return phoneField != null && phoneField.matches("[0-9]+")
                && (phoneField.length() > 8) && (phoneField.length() < 14);
    }
}
