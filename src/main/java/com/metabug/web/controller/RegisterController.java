package com.metabug.web.controller;

import com.metabug.persistence.model.User;
import com.metabug.service.UserService;
import com.metabug.web.dto.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
public class RegisterController {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

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
        final User user = userService.findUserByEmail(userDto.getEmail());

        if (user != null) {
            bindingResult.rejectValue("email", "error.user",
                    "User with provided email already exists");
            LOGGER.error("User with email {} already exists", user.getEmail());
        }

        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("register");
        } else {
            userService.save(userDto);
            modelAndView.addObject("successMessage", "User has been registered successfully");
            modelAndView.addObject("user", new User());
            modelAndView.setViewName("index");
            LOGGER.debug("Registering user account with information: {}", userDto);
        }
        return modelAndView;
    }
}
