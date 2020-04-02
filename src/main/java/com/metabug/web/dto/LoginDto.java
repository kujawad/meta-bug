package com.metabug.web.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class LoginDto {
    @NotNull
    @Size(min = 1)
    private String login;

    @NotNull
    @Size(min = 1)
    private String password;

    @Override
    public String toString() {
        return "LoginDto{" +
                "login='" + login + '\'' +
                '}';
    }
}
