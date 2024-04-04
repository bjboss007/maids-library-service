package com.maids.librarysystem.exception;

import com.maids.librarysystem.dto.response.ErrorResponse;
import com.maids.librarysystem.enums.ErrorType;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@RestController
public class GlobalExceptionHandler implements ErrorController {

    private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    public static final String DEFAULT_ERROR_PATH = "/error";

    public GlobalExceptionHandler() {
        logger.debug("global exception handler initialised");
    }

    @ExceptionHandler(LibraryApplicationException.class)
    public ResponseEntity<ErrorResponse> handleThrowable(LibraryApplicationException e) {
        logger.warn(e.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(e.getHttpStatus().value())
                .message(e.getMessage())
                .type(e.getHttpStatus().is5xxServerError() ? ErrorType.SERVER_ERROR : ErrorType.ERROR)
                .errors(e.getErrors())
                .build();

        return ResponseEntity.status(e.getHttpStatus()).body(errorResponse);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ErrorResponse> handleInternalServerError(Exception e) {
        logger.error("global exception handler: " + e.getMessage(), e);

        String errorMessage = "Something went wrong internally";
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(errorMessage)
                .type(ErrorType.SERVER_ERROR)
                .errors(Collections.singletonList(errorMessage))
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleError(MethodArgumentNotValidException e) {

        List<String> errors = e.getBindingResult().getAllErrors().parallelStream().map((error) -> ((FieldError) error).getField() + " " + error.getDefaultMessage()).collect(Collectors.toList());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(HttpStatus.UNPROCESSABLE_ENTITY.value())
                .message("Missing required parameter(s)")
                .type(ErrorType.VALIDATION_ERROR)
                .errors(errors)
                .build();

        return ResponseEntity.unprocessableEntity().body(errorResponse);
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<ErrorResponse> handleError(MissingRequestHeaderException e) {

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message(e.getMessage())
                .type(ErrorType.ERROR)
                .errors(Collections.singletonList(e.getMessage()))
                .build();

        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleError(HttpRequestMethodNotSupportedException e) {

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(HttpStatus.METHOD_NOT_ALLOWED.value())
                .message(e.getMessage())
                .type(ErrorType.ERROR)
                .errors(Collections.singletonList(e.getMessage()))
                .build();

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(errorResponse);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleError(MissingServletRequestParameterException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message(e.getMessage())
                .type(ErrorType.ERROR)
                .errors(Collections.singletonList(e.getMessage()))
                .build();
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleError(HttpMessageNotReadableException e) {
        String errorMessage = e.getMessage();
        logger.error(errorMessage, e);

        String[] split = errorMessage != null ? errorMessage.split(":") : null;

        errorMessage = (split == null || split.length == 0) ? errorMessage : split[0];

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message(errorMessage)
                .type(ErrorType.ERROR)
                .errors(Collections.singletonList(errorMessage))
                .build();

        return ResponseEntity.badRequest().body(errorResponse);
    }

    @RequestMapping(DEFAULT_ERROR_PATH)
    public ResponseEntity<ErrorResponse> handleError(HttpServletRequest request, HttpServletResponse response) {
        HttpStatus httpStatus = getHttpStatus(request);
        logger.debug("status code in handleError: " + httpStatus);

        String message = getErrorMessage(request, httpStatus);
        logger.debug("error message in handleError: " + message);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(httpStatus.value())
                .message(message)
                .type(httpStatus.is5xxServerError() ? ErrorType.SERVER_ERROR : ErrorType.ERROR)
                .errors(Collections.singletonList(message))
                .build();

        return ResponseEntity.status(httpStatus).body(errorResponse);
    }

    private HttpStatus getHttpStatus(HttpServletRequest request) {

        String code = request.getParameter("code");
        Integer status = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        HttpStatus httpStatus;

        if (status != null) httpStatus = HttpStatus.valueOf(status);
        else if (!Strings.isEmpty(code)) httpStatus = HttpStatus.valueOf(code);
        else httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

        return httpStatus;
    }

    private String getErrorMessage(HttpServletRequest request, HttpStatus httpStatus) {
        String message = (String) request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
        if(message != null && !message.isEmpty()) return message;

        switch (httpStatus) {
            case NOT_FOUND:
                message = "The resource does not exist";
                break;
            case INTERNAL_SERVER_ERROR:
                message = "Something went wrong internally";
                break;
            case FORBIDDEN:
                message = "Permission denied";
                break;
            case TOO_MANY_REQUESTS:
                message = "Too many requests";
                break;
            default:
                message = httpStatus.getReasonPhrase();
        }

        return message;
    }
}
