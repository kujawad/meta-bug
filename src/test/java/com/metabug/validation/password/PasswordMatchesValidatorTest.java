package com.metabug.validation.password;

import com.metabug.validation.password.PasswordMatchesValidator;
import com.metabug.web.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintValidatorContext;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

public class PasswordMatchesValidatorTest {

    private final ConstraintValidatorContext validatorContext = mock(ConstraintValidatorContext.class);;
    private UserDto user;

    @BeforeEach
    public void beforeEach() {
        user = new UserDto();
    }

    @Test
    public void shouldReturnTrueWhenPasswordsMatch() {
        //given
        final PasswordMatchesValidator passwordMatchesValidator = new PasswordMatchesValidator();
        user.setPassword("!Password123");
        user.setMatchingPassword("!Password123");
        //when
        final Boolean result = passwordMatchesValidator.isValid(user, validatorContext);
        //then
        assertTrue(result);
    }

    @Test
    public void shouldReturnFalseWhenPasswordsDontMatch() {
        //given
        final PasswordMatchesValidator passwordMatchesValidator = new PasswordMatchesValidator();
        user.setPassword("!Password123");
        user.setMatchingPassword("!Password321");
        //when
        final Boolean result = passwordMatchesValidator.isValid(user, validatorContext);
        //then
        assertFalse(result);
    }

}
