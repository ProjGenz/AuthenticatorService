package com.authenticationService.model.dao;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "cookie_entity")
public class CookieEntity {
    @Column(name = "user_email", nullable = false, unique = true)
    private String email;

    @Column(name = "cookie_value", nullable = false)
    private Long expiryTime;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "name", nullable = false)
    private String name;
}
