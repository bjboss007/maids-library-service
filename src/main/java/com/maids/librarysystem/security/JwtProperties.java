package com.maids.librarysystem.security;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "jwt")
@Data
@Component
@NoArgsConstructor
public class JwtProperties {
    public String secret;
    public int expirationTime;
    public String tokenPrefix;
    public String headerString;
}
