package com.authenticationService.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController("/auth")
public class AuthController {

    @PostMapping("/register")
    public String register(@RequestParam(name = "email", required = true) String email,
                           @RequestParam(name = "password", required = true) String password,
                           @RequestParam(name = "confirmPassword", required = true) String confirmPassword,
                           @RequestParam(name = "username", required = true) String username
    )
    {
        // Registration logic goes here
        return "User registered successfully";
    }
}