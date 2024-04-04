package com.maids.librarysystem.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.maids.librarysystem.exception.LibraryApplicationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private final UserPrincipalDetailService userService;
    private final JwtProperties jwtProperties;

    public JwtAuthorizationFilter(UserPrincipalDetailService userService, JwtProperties jwtProperties) {
        this.userService = userService;
        this.jwtProperties = jwtProperties;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader(jwtProperties.headerString);
        if(header == null || !header.startsWith(jwtProperties.tokenPrefix)){
            filterChain.doFilter(request, response);
            return;
        }

        try{
            Authentication authentication = getUsernamePasswordAuthentication(request);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }catch (Exception ex){
            response.setHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        }

        filterChain.doFilter(request, response);
    }

    private Authentication getUsernamePasswordAuthentication(HttpServletRequest request) throws LibraryApplicationException {
        String token = request.getHeader(jwtProperties.headerString);

        try{
            DecodedJWT verify = JWT.require(Algorithm.HMAC512(jwtProperties.secret))
                    .build()
                    .verify(token.replace(jwtProperties.tokenPrefix, ""));
            String username = verify.getSubject();

            if(username != null){
                UserDetails principal = userService.loadUserByUsername(username);
                return new UsernamePasswordAuthenticationToken(
                        username, null, principal.getAuthorities()
                );
            }
            return null;

        }catch (SignatureVerificationException | JWTDecodeException ex){
            request.setAttribute("error", "Invalid JWT signature");
            throw new LibraryApplicationException(HttpStatus.UNAUTHORIZED, "Invalid JWT signature");
        }catch (TokenExpiredException ex){
            request.setAttribute("error", "Expired JWT token");
            throw new LibraryApplicationException(HttpStatus.UNAUTHORIZED, "Expired JWT token");
        }catch (IllegalArgumentException ex){
            request.setAttribute("error", "JWT claims string is empty");
            throw new LibraryApplicationException(HttpStatus.UNAUTHORIZED, "JWT claims string is empty");
        }
    }

}
