package com.metabug.web.controller;

import com.metabug.persistence.model.User;
import com.metabug.service.UserService;
import com.metabug.web.dto.LoginDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
public class IndexController {

    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public IndexController(final UserService userService, final BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @GetMapping(value = {"/", "/login"})
    public ModelAndView index() {
        final ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");
        return modelAndView;
    }

    @PostMapping(value = {"/", "/login"})
    public ModelAndView login(@Valid final LoginDto loginDto,
                              final BindingResult bindingResult,
                              final HttpServletRequest request) {
        final ModelAndView modelAndView = new ModelAndView();

        if(bindingResult.hasErrors()) {
            addWarning(modelAndView);
            modelAndView.setViewName("index");
            return modelAndView;
        }

        final User user = userService.findUserByLogin(loginDto.getLogin());

        if(user != null) {
            if(bCryptPasswordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
                request.getSession().setAttribute("LOGGED_USER", loginDto.getLogin());
                modelAndView.setViewName("home");
            } else {
                addWarning(modelAndView);
                modelAndView.setViewName("index");
            }
        } else {
            addWarning(modelAndView);
            modelAndView.setViewName("index");
        }
        return modelAndView;
    }

    private void addWarning(final ModelAndView modelAndView) {
        modelAndView.addObject("warningMessage", "Invalid login or password");
    }
}
