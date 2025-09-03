package com.authenticationService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    private String message;
    private String username;
    private String email;
//    private String token;   // optional, if you also want to send JWT in response body
//    private long expiresIn; // optional, if you want frontend to know expiry time
}

