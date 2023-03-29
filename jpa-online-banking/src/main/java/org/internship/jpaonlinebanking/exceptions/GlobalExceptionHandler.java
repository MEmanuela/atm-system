package org.internship.jpaonlinebanking.exceptions;

import jakarta.persistence.RollbackException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.validation.FieldError;

import java.nio.file.InvalidPathException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public final ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex,
                                                                        HttpServletRequest request) {
        String error = ex.getMessage();
        return buildResponseEntity(new ErrorResponse(HttpStatus.NOT_FOUND, error));
    }
    @ExceptionHandler(TransactionException.class)
    public final ResponseEntity<Object> handleTransactionException(TransactionException ex,
                                                                   HttpServletRequest request) {
        String error = ex.getMessage();
        return buildResponseEntity(new ErrorResponse(HttpStatus.BAD_REQUEST, error));
    }
    @ExceptionHandler(AuthorizationException.class)
    public final ResponseEntity<Object> handleAuthorizationException(AuthorizationException ex,
                                                                      HttpServletRequest request) {
        String error = ex.getMessage();
        ErrorResponse response = new ErrorResponse(HttpStatus.UNAUTHORIZED);
        response.setMessage(error);

        return buildResponseEntity(response);
    }
    @ExceptionHandler(UserAuthenticationException.class)
    public final ResponseEntity<Object> handleUserAuthenticationException(UserAuthenticationException ex,
                                                                          HttpServletRequest request) {
        String error = ex.getMessage();
        return buildResponseEntity(new ErrorResponse(HttpStatus.FORBIDDEN, error));
    }
    @ExceptionHandler(UniqueConstraintException.class)
    public final ResponseEntity<Object> handleUniqueConstraintException(UniqueConstraintException ex,
                                                                        HttpServletRequest request) {
        String error = ex.getMessage();
        return buildResponseEntity(new ErrorResponse(HttpStatus.BAD_REQUEST, error));
    }
    @ExceptionHandler(InvalidPathException.class)
    public final ResponseEntity<Object> handleInvalidPathException(InvalidPathException ex,
                                                                   HttpServletRequest request) {
        String error = ex.getMessage();
        return buildResponseEntity(new ErrorResponse(HttpStatus.BAD_REQUEST, error));
    }
    @ExceptionHandler(BadCredentialsException.class)
    public final ResponseEntity<Object> handleBadCredentialsException(BadCredentialsException ex,
                                                                      HttpServletRequest request) {
        String error = ex.getMessage();
        return buildResponseEntity(new ErrorResponse(HttpStatus.BAD_REQUEST, error));
    }
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public final ResponseEntity<Object> handleArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex,
                                                                      HttpServletRequest request) {
        String error = ex.getName() + " should be of type " + ex.getRequiredType().getName();
        return buildResponseEntity(new ErrorResponse(HttpStatus.BAD_REQUEST, error));
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public final ResponseEntity<Object> handleArgumentNotValidException(MethodArgumentNotValidException ex,
                                                                        HttpServletRequest request) {
        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream().map(FieldError::getDefaultMessage).collect(Collectors.toList());
        return buildResponseEntity(new ErrorResponse(HttpStatus.BAD_REQUEST, String.valueOf(errors)));
    }
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public final ResponseEntity<Object> handleMissingParameterException(MissingServletRequestParameterException ex,
                                                                        HttpServletRequest request) {
        String error = ex.getParameterName() + " parameter is missing";
        return buildResponseEntity(new ErrorResponse(HttpStatus.BAD_REQUEST, error));
    }
    @ExceptionHandler(ConstraintViolationException.class)
    public final ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex,
                                                                           HttpServletRequest request) {
        List<String> errors = new ArrayList<String>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errors.add(violation.getRootBeanClass().getName() + " " +
                    violation.getPropertyPath() + ": " + violation.getMessage());
        }
        ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST);
        response.setMessage(String.valueOf(errors));
        return buildResponseEntity(response);
    }
    @ExceptionHandler(RollbackException.class)
    public final ResponseEntity<Object> handleRollbackException(RollbackException ex,
                                                                   HttpServletRequest request) {
        String error = ex.getMessage();
        return buildResponseEntity(new ErrorResponse(HttpStatus.BAD_REQUEST, error));
    }
    @ExceptionHandler(EmptyResultDataAccessException.class)
    public final ResponseEntity<Object> handleEmptyResultException(EmptyResultDataAccessException ex,
                                                        HttpServletRequest request) {
        String error = "The specified entity does not exists";
        return buildResponseEntity(new ErrorResponse(HttpStatus.BAD_REQUEST, error));
    }
    @ExceptionHandler(NoHandlerFoundException.class)
    public final ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex,
                                                                      HttpServletRequest request) {
        String error = "No handler found for " + ex.getHttpMethod() + " " + ex.getRequestURL();
        return buildResponseEntity(new ErrorResponse(HttpStatus.NOT_FOUND, error));
    }
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public final ResponseEntity<Object> handleHttpMethodNotSupportedException(HttpRequestMethodNotSupportedException ex,
                                                                             HttpServletRequest request) {
        String error = ex.getMethod() + " method is not supported for this request. Supported methods are " +
                ex.getSupportedHttpMethods();
        return buildResponseEntity(new ErrorResponse(HttpStatus.METHOD_NOT_ALLOWED, error));
    }
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public final ResponseEntity<Object> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException ex,
                                                                                 HttpServletRequest request) {
        String error = ex.getContentType() + " media type is not supported. Supported media types are " +
                ex.getSupportedMediaTypes();
        return buildResponseEntity(new ErrorResponse(HttpStatus.UNSUPPORTED_MEDIA_TYPE, error));
    }
    @ExceptionHandler(DataAccessException.class)
    public final ResponseEntity<Object> handleDataAccessException(DataAccessException ex,
                                                                  HttpServletRequest request) {
        String error = ex.getMessage();
        return buildResponseEntity(new ErrorResponse(HttpStatus.FORBIDDEN, error));
    }
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllExceptions(Exception ex, HttpServletRequest request) {
        String error = ex.getMessage();
        return buildResponseEntity(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, error));
    }
    private ResponseEntity<Object> buildResponseEntity(ErrorResponse errorResponse) {
        return new ResponseEntity<Object>(errorResponse, errorResponse.getStatus());
    }
}
