package com.metabug.web.controller;

import com.google.common.collect.Lists;
import com.metabug.persistence.model.Ticket;
import com.metabug.persistence.model.TicketStatus;
import com.metabug.service.TicketService;
import com.metabug.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.Comparator;
import java.util.List;

@Controller
public class HomeController {

    private final TicketService ticketService;
    private final UserService userService;

    @Autowired
    public HomeController(final TicketService ticketService,
                          final UserService userService) {
        this.ticketService = ticketService;
        this.userService = userService;
    }

    @GetMapping(value = {"/home"})
    public ModelAndView home(final Principal principal) {
        final ModelAndView modelAndView = new ModelAndView();
        final String login = principal.getName();

        modelAndView.setViewName("home");
        modelAndView.addObject("username", login);

        modelAndView.addObject("myTasksList", this.getMyTasks(login));
        modelAndView.addObject("unassignedTasksList", this.getUnassignedTasks());
        modelAndView.addObject("allTasksList", this.getAllTasks());

        modelAndView.addObject("userService", userService);

        return modelAndView;
    }

    private List<Ticket> getMyTasks(final String login) {
        final List<Ticket> list = ticketService.findAllByDeveloperIdAndStatus(
                userService.findUserByLogin(login).getId(),
                TicketStatus.ONGOING);
        if (list != null) {
            list.sort(Comparator.comparing(Ticket::getId));
        }
        return Lists.reverse(list);
    }

    private List<Ticket> getUnassignedTasks() {
        final List<Ticket> list = ticketService.findAllByStatus(TicketStatus.OPEN);
        if (list != null) {
            list.sort(Comparator.comparing(Ticket::getId));
        }
        return Lists.reverse(list);
    }

    private List<Ticket> getAllTasks() {
        final List<Ticket> list = ticketService.findAll();
        if (list != null) {
            list.sort(Comparator.comparing(Ticket::getId));
        }
        return Lists.reverse(list);
    }
}
