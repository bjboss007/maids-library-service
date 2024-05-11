package com.maids.librarysystem.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maids.librarysystem.dto.response.ErrorResponse;
import com.maids.librarysystem.enums.ErrorType;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.util.Collections;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        response.setHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getOutputStream().write(new ObjectMapper().writeValueAsBytes(ErrorResponse.builder().code(HttpStatus.UNAUTHORIZED.value())
                        .type(ErrorType.ERROR)
                        .message(authException.getMessage())
                        .errors(Collections.singletonList(authException.getMessage()))
                        .build()
                ));
    }
}