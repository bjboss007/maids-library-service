package com.maids.librarysystem.config;


import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class LoginViewModel {
    @NotEmpty(message = "Please supply your username")
    private String username;
    @NotEmpty(message = "Please supply your password")
    private String password;
}
