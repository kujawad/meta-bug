package com.metabug.web.controller;

import com.metabug.service.TicketService;
import com.metabug.web.dto.TicketDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AddTicketController {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    private final TicketService ticketService;

    @Autowired
    public AddTicketController(final TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping(value = {"/add-ticket"})
    public ModelAndView getView() {
        final ModelAndView modelAndView = new ModelAndView();
        final TicketDto ticketDto = new TicketDto();

        modelAndView.setViewName("add-ticket");
        modelAndView.addObject("ticketDto", ticketDto);

        return modelAndView;
    }

    @PostMapping(value = {"/add-ticket"})
    public String addTicket(final TicketDto ticketDto,
                            final Model model) {
        ticketService.save(ticketDto);
        LOGGER.info("Saved ticket: " + ticketDto.toString());
        model.addAttribute("message", "Successfully added ticket!");
        return "redirect:/home";
    }
}
