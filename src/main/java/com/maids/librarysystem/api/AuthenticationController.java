package com.maids.librarysystem.api;

import com.maids.librarysystem.config.AppResponse;
import com.maids.librarysystem.config.LoginViewModel;
import com.maids.librarysystem.dto.response.AuthResponse;
import com.maids.librarysystem.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth/login")
public class AuthenticationController {
    @Autowired
    private AuthenticationService authenticationService;


    @PostMapping
    public ResponseEntity<AppResponse<AuthResponse>> authenticateUser(@RequestBody @Valid LoginViewModel loginViewModel){
        AuthResponse authResponse = authenticationService.generateToken(loginViewModel);
        return AppResponse.build(authResponse);
    }
}
