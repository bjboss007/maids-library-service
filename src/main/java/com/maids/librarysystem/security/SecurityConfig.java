package com.maids.librarysystem.security;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final UserPrincipalDetailService userDetailService;
    private final JwtProperties jwtProperties;

    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(csrf -> csrf.disable())
                .exceptionHandling((httpSecurityExceptionHandlingConfigurer -> {

                    httpSecurityExceptionHandlingConfigurer.authenticationEntryPoint(new CustomAuthenticationEntryPoint());
                    httpSecurityExceptionHandlingConfigurer.accessDeniedHandler(new CustomAccessDeniedHandler());
                }))
                .authorizeHttpRequests(authorize -> {
                            authorize.requestMatchers("/api/auth/login").permitAll()
                                    .requestMatchers(HttpMethod.POST, "/api/books").authenticated()
                                    .requestMatchers(HttpMethod.PUT, "/api/books/**").authenticated()
                                    .requestMatchers(HttpMethod.DELETE, "/api/books/**").authenticated()
                                    .requestMatchers(HttpMethod.POST, "/api/patrons").authenticated()
                                    .requestMatchers(HttpMethod.PUT, "/api/patrons/**").authenticated()
                                    .requestMatchers(HttpMethod.GET, "/**").permitAll();
                        }
                ).authenticationProvider(authenticationProvider)
                .addFilterBefore(new JwtAuthorizationFilter(userDetailService, jwtProperties), UsernamePasswordAuthenticationFilter.class).
                sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return httpSecurity.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Collections.singletonList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET","POST","PUT","OPTION"));
        configuration.setAllowedHeaders(Collections.singletonList("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
