package com.metabug.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class IndexController {

    @GetMapping(value = {"/", "/login"})
    public ModelAndView index() {
        final ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");
        return modelAndView;
    }

    @PostMapping(value = {"/", "/login"})
    public ModelAndView login() {
        final ModelAndView modelAndView = new ModelAndView();

        return modelAndView;
    }
}
