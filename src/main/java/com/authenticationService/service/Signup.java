package com.authenticationService.service;

import jakarta.servlet.http.HttpServletResponse;

public interface Signup {
//    HttpServletResponse saveAndReturnCookie (String email, String password, String username, HttpServletResponse response);
    void register (String email, String password, String username, HttpServletResponse response);
    long getRemainingSessionTime(String email);
}
