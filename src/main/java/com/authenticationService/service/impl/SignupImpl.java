package com.authenticationService.service.impl;

import com.authenticationService.repository.UserRepository;
import com.authenticationService.service.Signup;
import jakarta.servlet.http.Cookie;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Objects;
import java.util.UUID;

@Service
public class SignupImpl implements Signup {
    private final UserRepository userRepository;

    @Autowired
    public SignupImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public HttpServletResponse saveAndReturnCookie (String email, String password, String confirmPassword, String username, HttpServletResponse response) {
//        if (email != null && password != null && password.equals(confirmPassword) && username != null) {
            //email validation reges (@xy.com) should be done from ui side to remove unnecessary api calls and failures each time
    //first signup => userid, email, name, etc save to db, fetch uid using email id, make a fresh cookie, save to redis or any cache -> login
        try {
            UUID userId = userRepository.findUserIdByEmailId(email);
            String cookieValue = String.format("email=%s;uname=%s;userid=%s", email, username, userId);
            Cookie cookie = new Cookie("userSession", cookieValue);
            cookie.setMaxAge((int) Duration.ofDays(20).getSeconds());
            cookie.setHttpOnly(true); // Optional: Makes the cookie inaccessible to JavaScript
            cookie.setPath("/"); // Optional: Sets the cookie path
            response.addCookie(cookie);
        }
        catch (Exception ignored) {

        }

        return response;
    }

    @Override
    public void registter(String email, String password, String confirmPassword, String username) {

    }
}
