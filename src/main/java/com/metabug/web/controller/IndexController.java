package com.metabug.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

@Controller
public class IndexController {

    @GetMapping(value = {"/login", ""})
    public ModelAndView index(final Principal principal) {
        final ModelAndView modelAndView = new ModelAndView();
        if(principal != null) {
            modelAndView.setViewName("redirect:/home");
        } else {
            modelAndView.setViewName("index");
        }
        return modelAndView;
    }
}
