package com.authenticationService.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    // This can be either email or username
    private String identifier;

    private String password;
}
