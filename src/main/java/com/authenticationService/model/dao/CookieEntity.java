package com.authenticationService.model.dao;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.UUID;

// do we really need this class???????

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "cookie_entity")
public class CookieEntity {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;  // Primary key

    @Column(name = "user_email", nullable = false, unique = true)
    private String email;

    @Column(name = "cookie_value", nullable = false)
    private Long expiryTime;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "name", nullable = false)
    private String name;
}
