package com.metabug.web.controller;

import com.metabug.persistence.model.User;
import com.metabug.service.UserService;
import com.metabug.web.dto.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

@Controller
public class RegisterController {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    private final String PASSWORD = "password";
    private final String PASSWORD_MATCHES = "PasswordMatches";
    private final String EMAIL = "email";

    private final UserService userService;

    @Autowired
    public RegisterController(final UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/register")
    public ModelAndView registerPage() {
        final ModelAndView modelAndView = new ModelAndView();
        final UserDto userDto = new UserDto();

        modelAndView.addObject("userDto", userDto);
        modelAndView.setViewName("register");
        return modelAndView;
    }

    @PostMapping(value = "/register")
    public ModelAndView register(@Valid final UserDto userDto,
                                 final BindingResult bindingResult) {
        final ModelAndView modelAndView = new ModelAndView();

        if (isCorrect(userDto, bindingResult, modelAndView)) {
            userService.save(userDto);
            modelAndView.addObject("successMessage", "User has been registered successfully");
            modelAndView.setViewName("index");
            LOGGER.debug("Registering user account with information: {}", userDto);
        }
        return modelAndView;
    }

    private boolean isCorrect(final UserDto userDto, final BindingResult bindingResult, final ModelAndView modelAndView) {
        final User userByEmail = userService.findUserByEmail(userDto.getEmail());
        final User userByLogin = userService.findUserByLogin(userDto.getLogin());

        if (userByLogin != null) {
            modelAndView.addObject("warningMessage", "Login already taken");
            LOGGER.error("User with login {} already exists", userDto.getLogin());
            return false;
        } else if (userByEmail != null) {
            modelAndView.addObject("warningMessage", "Email already taken");
            LOGGER.error("User with email {} already exists", userDto.getEmail());
            return false;
        }

        if(bindingResult.hasErrors()) {
            List<ObjectError> errorList = bindingResult.getAllErrors();

            for (ObjectError error : errorList) {
                if (error instanceof FieldError) {
                    switch (((FieldError) error).getField()) {
                        case EMAIL:
                            modelAndView.addObject("warningMessage", "Incorrect email");
                            LOGGER.error("Provided email is incorrect");
                            return false;
                        case PASSWORD:
                            modelAndView.addObject("warningMessage",
                                    "Incorrect password: 8 characters, 1 letter, 1 capital letter, 1 digit and 1 special character required");
                            LOGGER.error("Provided password is incorrect");
                            return false;
                        default:
                            modelAndView.addObject("warningMessage", "All fields must be filled");
                            LOGGER.error("All registration fields must be filled");
                            return false;
                    }
                } else if (error.getCode().equals(PASSWORD_MATCHES)){
                    modelAndView.addObject("warningMessage", "Passwords must match");
                    LOGGER.error("Provided passwords do not match");
                    return false;
                } else {
                    modelAndView.addObject("warningMessage", "Unknown error");
                    LOGGER.error("Unknown error occurred while registering");
                    return false;
                }
            }
        }
        return true;
    }
}
