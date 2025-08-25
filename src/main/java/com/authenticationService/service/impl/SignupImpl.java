package com.authenticationService.service.impl;

import com.authenticationService.model.dao.CookieEntity;
import com.authenticationService.model.dao.User;
import com.authenticationService.repository.CookieRepository;
import com.authenticationService.repository.UserRepository;
import com.authenticationService.service.Signup;
import jakarta.servlet.http.Cookie;


import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Objects;
import java.util.UUID;

@Service
public class SignupImpl implements Signup {

    Logger LOGGER = org.slf4j.LoggerFactory.getLogger(SignupImpl.class);
    private final UserRepository userRepository;
    private final CookieRepository cookieRepository;

    @Autowired
    public SignupImpl(UserRepository userRepository, CookieRepository cookieRepository) {
        this.userRepository = userRepository;
        this.cookieRepository = cookieRepository;
    }
    @Override
    public HttpServletResponse saveAndReturnCookie(String email, String password, String username, HttpServletResponse response) {
        try {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found for email: " + email));

//            String cookieValue = String.format("email=%s;uname=%s;userid=%s",
//                    user.getEmail(), user.getUsername(), user.getId());
//
//            Cookie cookie = new Cookie("userSession", cookieValue);
            String rawValue = String.format("email=%s;uname=%s;userid=%s",
                    user.getEmail(), user.getUsername(), user.getId());

            String encodedValue = URLEncoder.encode(rawValue, StandardCharsets.UTF_8);

            Cookie cookie = new Cookie("userSession", encodedValue);
            cookie.setMaxAge((int) Duration.ofDays(20).getSeconds());
            cookie.setHttpOnly(true);
            cookie.setPath("/");

            response.addCookie(cookie);

            LOGGER.info("Cookie saved successfully for user: {}", email);
        } catch (Exception e) {
            LOGGER.error("Error while saving cookie: {}", e.getMessage());
            throw new RuntimeException("Error while saving cookie: " + e.getMessage());
        }
        return response;
    }

//    @Override
//    public HttpServletResponse saveAndReturnCookie (String email, String password, String username, HttpServletResponse response) {
////        if (email != null && password != null && password.equals(confirmPassword) && username != null) {
//            //email validation reges (@xy.com) should be done from ui side to remove unnecessary api calls and failures each time
//    //first signup => uname, email, password, etc save to db, fetch uid using email id, make a fresh cookie, save to redis or any cache -> login
//        try {
//            UUID userId = userRepository.findUserIdByEmailId(email);
//            String cookieValue = String.format("email=%s;uname=%s;userid=%s", email, username, userId);
//            Cookie cookie = new Cookie("userSession", cookieValue);
//            cookie.setMaxAge((int) Duration.ofDays(20).getSeconds());
//            cookie.setHttpOnly(true); // Optional: Makes the cookie inaccessible to JavaScript ->  HttpOnly flag enhances security. This prevents client-side scripts from accessing the cookie, reducing the risk of cross-site scripting (XSS) attacks. If an attacker injects malicious JavaScript into your application, they won't be able to steal cookies marked as HttpOnly. This is particularly important for sensitive cookies, such as session cookies, to protect user data and maintain secure authentication.
//            cookie.setPath("/"); // Optional: Sets the cookie path
//            response.addCookie(cookie);
//            CookieEntity cookieEntity = new CookieEntity(email, Duration.ofDays(20).getSeconds(), username, userRepository.findNameByEmailId(email));
//            cookieRepository.saveCookieByEmail(cookieEntity);
//            LOGGER.info("Cookie saved successfully");
//        }
//        catch (Exception ignored) {
//            LOGGER.error("Error while saving cookie: {}", ignored.getMessage());
//            throw new RuntimeException("Error while saving cookie: " + ignored.getMessage());
//        }
//        return response;
//    }

//    @Override
//    public void register(String email, String password, String username, HttpServletResponse response) {
//        if (userRepository.findUserIdByEmailId(email) == null) {
//            User user = new User(email, username, password);
//            userRepository.save(user);
//            LOGGER.info("User registered successfully...");
//
//            HttpServletResponse cookieResponse = saveAndReturnCookie(email, password, username, response);
//        }
//        else {
//            LOGGER.error("User already exists!");
//            throw new RuntimeException("User already exists !");
//        }
//    }
//    @Override
    @Autowired
    private PasswordEncoder passwordEncoder;  // inject in SignupImpl

    @Override
    public void register(String email, String password, String username, HttpServletResponse response) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("User already exists with this email!");
        }
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("User already exists with this username!");
        }

        // Hash password before saving
        String hashedPassword = passwordEncoder.encode(password);

        User user = new User(email, username, hashedPassword);
        userRepository.save(user);

        // Save cookie
        saveAndReturnCookie(email, hashedPassword, username, response);
    }

//    @Override
//    public long getRemainingSessionTime(String email) {
//        try {
//            UUID userId = userRepository.findUserIdByEmailId(email);
//            // Logic to calculate remaining time (you may need to store the creation time of the session)
//            // For simplicity, returning the fixed timeout here
//            return Duration.ofDays(20).getSeconds();
//        } catch (Exception ignored) {
//            LOGGER.error("Error while fetching remaining session time: {}", ignored.getMessage());
//            throw new RuntimeException("Error while fetching remaining session time: " + ignored.getMessage());
//        }
//    }
@Override
public long getRemainingSessionTime(String email) {
    try {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        UUID userId = user.getId();  // ✅ use getUserId()

        // TODO: Replace with actual session logic
        // Right now just returning a fixed timeout of 20 days
        return Duration.ofDays(20).getSeconds();

    } catch (Exception ex) {
        LOGGER.error("Error while fetching remaining session time: {}", ex.getMessage());
        throw new RuntimeException("Error while fetching remaining session time: " + ex.getMessage());
    }
}

}
