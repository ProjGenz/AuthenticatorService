package com.authenticationService.service;

import jakarta.servlet.http.HttpServletResponse;

public interface Signup {
    HttpServletResponse saveAndReturnCookie (String email, String password, String confirmPassword, String username, HttpServletResponse response);

    void registter (String email, String password, String confirmPassword, String username);
}
