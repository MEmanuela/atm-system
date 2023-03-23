package org.internship.jpaonlinebanking.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UniqueConstraintException extends RuntimeException {
    public UniqueConstraintException() {
        super();
    }

    public UniqueConstraintException(String message) {
        super(message);
    }

    public UniqueConstraintException(String message, Throwable cause) {
        super(message, cause);
    }
}