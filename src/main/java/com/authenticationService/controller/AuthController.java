package com.authenticationService.controller;

import com.authenticationService.service.Signup;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.util.Objects;

@RestController("/auth")
public class AuthController {

    private final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);
    private final Signup signupService;

    @Autowired
    public AuthController (Signup signupService) {
        this.signupService = signupService;
    }

    @PostMapping("/register")
    public String register(
            @RequestParam(name = "email", required = true) String email,
            @RequestParam(name = "password", required = true) String password,
            @RequestParam(name = "username", required = true) String username,
            @RequestHeader(name = "api-version", required = false, defaultValue = "1.0.0") String apiVersion,
            HttpServletResponse httpServletResponse
    )
    {
        if (!Objects.equals(apiVersion, "1.0.0")) {
            LOGGER.error("Unsupported API version: {}", apiVersion);
            return new Exception("Unsupported API version: " + apiVersion).getMessage();
        }
        signupService.register(email, password, username, httpServletResponse);
        return "User registered successfully";
    }


    @GetMapping("/get-session-time")
    public long getRemainingSessionTime(        //CALL this api periodically from the ui (say each minute) to check remaining session time, when time <= 5 mins, ask user if he wants to refresh or not, if yes, call refresh api
            @RequestParam(name = "email", required = true) String email
    ) {
        return signupService.getRemainingSessionTime(email);
    }
}