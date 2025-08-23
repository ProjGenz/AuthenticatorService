package com.authenticationService.repository;

import com.authenticationService.model.dao.CookieEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.servlet.http.Cookie;

@Repository
public interface CookieRepository extends JpaRepository<String, Integer> {
    void saveCookieByEmail(CookieEntity cookieEntity);
    void saveCookie(Cookie cookie);
}
