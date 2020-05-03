package com.metabug.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.metabug.persistence.model.Ticket;
import com.metabug.service.TicketService;
import com.metabug.web.dto.TicketDto;
import com.metabug.web.dto.TicketViewDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

@Controller
public class TicketController {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    private final TicketService ticketService;

    @Autowired
    public TicketController(final TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping(value = {"/add-ticket"})
    public ModelAndView getView(final Principal principal) {
        final ModelAndView modelAndView = new ModelAndView();
        final TicketDto ticketDto = new TicketDto();

        modelAndView.setViewName("add-ticket");
        modelAndView.addObject("ticketDto", ticketDto);
        modelAndView.addObject("username", principal.getName());

        return modelAndView;
    }

    @PostMapping(value = {"/add-ticket"})
    public String addTicket(final TicketDto ticketDto,
                            final Model model,
                            final Principal principal) {
        ticketService.save(ticketDto, principal);
        LOGGER.info("Saved ticket: " + ticketDto.toString());
        model.addAttribute("message", "Successfully added ticket!");
        return "redirect:/home";
    }

    @ResponseBody
    @GetMapping(value = {"/get-tickets"}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public String getTickets() throws JsonProcessingException {
        final ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(ticketService.findAll());
    }

    @GetMapping(value = {"/ticket/{id}"})
    public String viewTicket(@PathVariable final long id, final Model model,
                             final Principal principal) {
        final Ticket ticket = ticketService.findById(id);
        if (ticket == null) {
            return "redirect:/home";
        }

        final TicketViewDto ticketViewDto = ticketService.toTicketView(ticket);
        model.addAttribute("ticketViewDto", ticketViewDto);
        model.addAttribute("username", principal.getName());
        return "view-ticket";
    }

    @PostMapping(value = {"/ticket/{id}"}, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String assignTicket(final TicketViewDto ticketViewDto, final Principal principal) {
        final Ticket ticket = ticketService.findById(ticketViewDto.getId());
        if (ticket.getDeveloperId() == null) {
            ticketService.assignTicket(ticket.getId(), principal.getName());
            LOGGER.info("Assigned ticket: " + ticket.getId() + " to " + principal.getName());
        }
        return "redirect:/ticket/" + ticketViewDto.getId();
    }
}
