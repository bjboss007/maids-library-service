package com.maids.librarysystem.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.maids.librarysystem.config.LoginViewModel;
import com.maids.librarysystem.dto.response.AuthResponse;
import com.maids.librarysystem.exception.LibraryApplicationException;
import com.maids.librarysystem.model.AppUser;
import com.maids.librarysystem.security.JwtProperties;
import com.maids.librarysystem.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtProperties properties;

    public Authentication attemptAuthentication(LoginViewModel credentials) throws AuthenticationException{

        Authentication authentication = null;
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                credentials.getUsername(),
                credentials.getPassword(),
                new ArrayList<>()
        );
        try {
            authentication = authenticationManager.authenticate(authenticationToken);
        }catch (Exception ex){
            throw new LibraryApplicationException(HttpStatus.UNAUTHORIZED, ex.getMessage());
        }
        return authentication;
    }

    public AuthResponse generateToken(LoginViewModel loginViewModel){
        Authentication authentication = attemptAuthentication(loginViewModel);
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        AppUser appUser = userPrincipal.getAppUser();

        String token = JWT.create()
               .withSubject(userPrincipal.getUsername())
               .withExpiresAt(new Date(System.currentTimeMillis() + properties.expirationTime))
               .sign(Algorithm.HMAC512(properties.secret.getBytes()));

        return AuthResponse.builder()
                .token(token)
                .username(appUser.getUsername())
                .bearer(properties.tokenPrefix)
                .build();
    }

}
