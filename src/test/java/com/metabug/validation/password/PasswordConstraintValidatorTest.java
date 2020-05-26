package com.metabug.validation.password;


import com.metabug.validation.password.PasswordConstraintValidator;
import org.junit.Test;

import javax.validation.ConstraintValidatorContext;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class PasswordConstraintValidatorTest {

    private final ConstraintValidatorContext validatorContext = mock(ConstraintValidatorContext.class);
    private final ConstraintValidatorContext.ConstraintViolationBuilder constraintViolationBuilder = mock(ConstraintValidatorContext.ConstraintViolationBuilder.class);

    @Test
    public void shouldReturnTrueWhenPasswordIsValid() {
        //given
        final PasswordConstraintValidator passwordConstraintValidator = new PasswordConstraintValidator();
        //when
        final Boolean result = passwordConstraintValidator.isValid("!1Aaaaaaa", validatorContext);
        //then
        assertTrue(result);
    }

    @Test
    public void shouldReturnFalseWhenPasswordIsTooShort() {
        //given
        final PasswordConstraintValidator passwordConstraintValidator = new PasswordConstraintValidator();
        doNothing().when(validatorContext).disableDefaultConstraintViolation();
        when(validatorContext.buildConstraintViolationWithTemplate("Password must be 8 or more characters in length.")
        ).thenReturn(constraintViolationBuilder);
        //when
        final Boolean result = passwordConstraintValidator.isValid("!1Aa", validatorContext);
        //then
        assertFalse(result);
    }

    @Test
    public void shouldReturnFalseWhenNoLowercase() {
        //given
        final PasswordConstraintValidator passwordConstraintValidator = new PasswordConstraintValidator();
        doNothing().when(validatorContext).disableDefaultConstraintViolation();
        when(validatorContext.buildConstraintViolationWithTemplate("Password must contain 1 or more lowercase characters.")
        ).thenReturn(constraintViolationBuilder);
        //when
        final Boolean result = passwordConstraintValidator.isValid("!1AAAAAAA", validatorContext);
        //then
        assertFalse(result);
    }

    @Test
    public void shouldReturnFalseWhenNoUppercase() {
        //given
        final PasswordConstraintValidator passwordConstraintValidator = new PasswordConstraintValidator();
        doNothing().when(validatorContext).disableDefaultConstraintViolation();
        when(validatorContext.buildConstraintViolationWithTemplate("Password must contain 1 or more uppercase characters.")
        ).thenReturn(constraintViolationBuilder);
        //when
        final Boolean result = passwordConstraintValidator.isValid("!1aaaaaaa", validatorContext);
        //then
        assertFalse(result);
    }

    @Test
    public void shouldReturnFalseWhenNoDigit() {
        //given
        final PasswordConstraintValidator passwordConstraintValidator = new PasswordConstraintValidator();
        doNothing().when(validatorContext).disableDefaultConstraintViolation();
        when(validatorContext.buildConstraintViolationWithTemplate("Password must contain 1 or more digit characters.")
        ).thenReturn(constraintViolationBuilder);
        //when
        Boolean result = passwordConstraintValidator.isValid("!!Aaaaaaa", validatorContext);
        //then
        assertFalse(result);
    }

    @Test
    public void shouldReturnFalseWhenNoSpecialCharacters() {
        //given
        final PasswordConstraintValidator passwordConstraintValidator = new PasswordConstraintValidator();
        doNothing().when(validatorContext).disableDefaultConstraintViolation();
        when(validatorContext.buildConstraintViolationWithTemplate("Password must contain 1 or more special characters.")
        ).thenReturn(constraintViolationBuilder);
        //when
        Boolean result = passwordConstraintValidator.isValid("11Aaaaaaa", validatorContext);
        //then
        assertFalse(result);
    }

    @Test
    public void shouldReturnFalseWhenPasswordContainsWhitespace() {
        //given
        final PasswordConstraintValidator passwordConstraintValidator = new PasswordConstraintValidator();
        doNothing().when(validatorContext).disableDefaultConstraintViolation();
        when(validatorContext.buildConstraintViolationWithTemplate("Password contains a whitespace character.")
        ).thenReturn(constraintViolationBuilder);
        //when
        final Boolean result = passwordConstraintValidator.isValid("!1Aaa aaaa", validatorContext);
        //then
        assertFalse(result);
    }
}