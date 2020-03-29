package com.metabug.web.dto;

import com.metabug.validation.email.ValidEmail;
import com.metabug.validation.password.PasswordMatches;
import com.metabug.validation.password.ValidPassword;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@PasswordMatches
public class UserDto {
    @NotNull
    @Size(min = 1)
    private String login;

    @NotNull
    @Size(min = 1)
    @ValidEmail
    private String email;

    @ValidPassword
    private String password;

    @NotNull
    @Size(min = 1)
    private String matchingPassword;

    @Override
    public String toString() {
        return "UserDto{" +
                "login='" + login + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
