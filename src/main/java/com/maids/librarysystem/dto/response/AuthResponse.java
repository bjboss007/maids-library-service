package com.maids.librarysystem.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@Builder
public class AuthResponse {
    private String token;
    private String bearer;
    private String username;
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();
}
