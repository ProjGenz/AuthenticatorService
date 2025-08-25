package com.authenticationService.model.dao;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID Id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "name")
    private String name;

    @Column(name = "problemsSolved", nullable = false)
    private int problemsSolved;

    @Column(name = "`rank`", nullable = false)
    private int rank;

    @Column(name = "contests", nullable = false)
    private int contestsAttended;

    public User (String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.problemsSolved = 0;
        this.rank = 0;
        this.contestsAttended = 0;
    }
}