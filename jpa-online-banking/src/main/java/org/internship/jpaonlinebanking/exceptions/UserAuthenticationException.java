package org.internship.jpaonlinebanking.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class UserAuthenticationException extends RuntimeException {
    public UserAuthenticationException() {
        super();
    }

    public UserAuthenticationException(String message) {
        super(message);
    }

    public UserAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}