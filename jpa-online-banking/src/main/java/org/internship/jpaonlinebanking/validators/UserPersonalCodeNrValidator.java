package org.internship.jpaonlinebanking.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UserPersonalCodeNrValidator implements ConstraintValidator<ValidPersonalCodeNr, String> {

    @Override
    public boolean isValid(String pcnField, ConstraintValidatorContext context) {
        return pcnField != null && pcnField.matches("[0-9]+")
                && pcnField.length() == 13;
    }
}
