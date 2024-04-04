package com.maids.librarysystem.exception;

import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;

public class LibraryApplicationException extends RuntimeException{

    private HttpStatus httpStatus;
    private List<String> errors;
    private Object data;

    public LibraryApplicationException(String message) {
        this(HttpStatus.BAD_REQUEST, message);
    }

    public LibraryApplicationException(HttpStatus httpStatus, String message) {
        this(httpStatus, message, Collections.singletonList(message), null);
    }

    public LibraryApplicationException(HttpStatus httpStatus, String message, List<String> errors, Object data) {
        super(message);
        this.httpStatus = httpStatus;
        this.errors = errors;
        this.data = data;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public List<String> getErrors() {
        return errors;
    }

    public Object getData() {
        return data;
    }
}
