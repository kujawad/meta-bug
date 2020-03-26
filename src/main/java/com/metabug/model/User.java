package com.metabug.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import java.util.UUID;

@Entity
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private UUID id;
    private String name;
    private String password;
    private String role;
    private String email;

    protected User() {}

    public User(final UUID id, final String name, final String password, final String role, final String email) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.role = role;
        this.email = email;
    }

}