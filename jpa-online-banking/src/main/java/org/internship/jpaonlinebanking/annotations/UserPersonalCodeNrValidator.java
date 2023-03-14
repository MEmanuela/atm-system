package org.internship.jpaonlinebanking.annotations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UserPersonalCodeNrValidator implements ConstraintValidator<UserPersonalCodeNrConstraint, String> {

    @Override
    public boolean isValid(String pcnField, ConstraintValidatorContext context) {
        return pcnField != null && pcnField.matches("[0-9]+")
                && (pcnField.length() == 13);
    }
}
