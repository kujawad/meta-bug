package com.metabug.validation.email;

import com.metabug.validation.email.EmailValidator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import javax.validation.ConstraintValidatorContext;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

public class EmailValidatorTest {

    private final ConstraintValidatorContext validatorContext = mock(ConstraintValidatorContext.class);

    @ParameterizedTest
    @ValueSource(strings = {"Test-Email123@gmail.com", "TestEmail123@mail.net", "testemail123@wp.pl", "Test.Email@o2.org", "96420@stud.uz.zgora.pl", "96_420@stud.uz.zgora.pl"})
    public void shouldReturnTrueWhenEmailIsValid(final String input) {
       //given
        final EmailValidator emailValidator = new EmailValidator();
        //when
        final Boolean result = emailValidator.isValid(input, validatorContext);
        //then
        assertTrue(result);
    }

    @ParameterizedTest
    @ValueSource(strings = {"TestEmail1!!@gmail.com", "Test Email123@mail.net", "testemail123wp.pl", "TestEmail@o2org", "96=420@stud.uz.zgora.pl"})
    public void shouldReturnFalseWhenEmailIsInvalid(final String input) {
        //given
        final EmailValidator emailValidator = new EmailValidator();
        //when
        final Boolean result = emailValidator.isValid(input, validatorContext);
        //then
        assertFalse(result);
    }

}
