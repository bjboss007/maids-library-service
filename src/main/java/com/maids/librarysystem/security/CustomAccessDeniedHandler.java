package com.maids.librarysystem.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maids.librarysystem.dto.response.ErrorResponse;
import com.maids.librarysystem.enums.ErrorType;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;
import java.util.Collections;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {

        response.setHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.getOutputStream().write(new ObjectMapper().writeValueAsBytes(ErrorResponse.builder().code(HttpStatus.FORBIDDEN.value())
                .type(ErrorType.ERROR)
                .message(accessDeniedException.getMessage())
                .errors(Collections.singletonList(accessDeniedException.getMessage()))
                .build()
        ));
    }
}
