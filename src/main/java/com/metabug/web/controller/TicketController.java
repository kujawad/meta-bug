package com.metabug.web.controller;

import com.metabug.persistence.model.Ticket;
import com.metabug.persistence.model.TicketStatus;
import com.metabug.persistence.model.User;
import com.metabug.service.EmailService;
import com.metabug.service.TicketService;
import com.metabug.service.UserService;
import com.metabug.web.dto.TicketDto;
import com.metabug.web.dto.TicketViewDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.UUID;

@Controller
public class TicketController {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    private final TicketService ticketService;
    private final UserService userService;
    private final EmailService emailService;

    @Autowired
    public TicketController(final TicketService ticketService,
                            final UserService userService,
                            final EmailService emailService) {
        this.ticketService = ticketService;
        this.userService = userService;
        this.emailService = emailService;
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

    @GetMapping(value = {"/ticket/{id}"})
    public String viewTicket(@PathVariable final long id, final Model model,
                             final Principal principal) {
        final Ticket ticket = ticketService.findById(id);
        if (ticket == null) {
            return "redirect:/home";
        }

        final User user = userService.findUserByLogin(principal.getName());
        final TicketViewDto ticketViewDto = ticketService.toTicketView(ticket);
        model.addAttribute("ticketViewDto", ticketViewDto);
        model.addAttribute("username", principal.getName());
        if (ticket.getStatus() == TicketStatus.OPEN) {
            return "view-ticket-open";
        } else if (ticket.getStatus() == TicketStatus.ONGOING &&
                ticket.getDeveloperId().equals(user.getId())) {
            return "view-ticket-ongoing";
        } else {
            return "view-ticket-done";
        }
    }

    @PostMapping(value = {"/ticket/{id}"}, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, params = "assign")
    public String assignTicket(final TicketViewDto ticketViewDto, final Principal principal) {
        final long ticketId = ticketViewDto.getId();
        final String developer = principal.getName();

        final Ticket ticket = ticketService.findById(ticketId);

        if (ticket.getDeveloperId() == null) {
            ticketService.assignTicket(ticketId, developer);

            emailService.send(
                    ticketId,
                    ticket.getTitle(),
                    TicketStatus.ONGOING.name(),
                    developer,
                    userService.findUserById(ticket.getAuthorId()).getEmail()
            );
            LOGGER.info("Assigned ticket: " + ticketId + " to " + developer);
        }
        return "redirect:/ticket/" + ticketId;
    }

    @PostMapping(value = {"/ticket/{id}"}, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, params = "markDone")
    public String markTicketDone(final TicketViewDto ticketViewDto) {
        final long ticketId = ticketViewDto.getId();
        ticketService.markTicketDone(ticketId);

        final Ticket ticket = ticketService.findById(ticketId);
        emailService.send(
                ticketId,
                ticket.getTitle(),
                TicketStatus.DONE.name(),
                userService.findUserById(ticket.getDeveloperId()).getLogin(),
                userService.findUserById(ticket.getAuthorId()).getEmail()
        );

        LOGGER.info("Marked ticket: " + ticketId + " as " + TicketStatus.DONE);
        return "redirect:/home";
    }

    @PostMapping(value = {"/ticket/{id}"}, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, params = "unassign")
    public String unassignTicket(final TicketViewDto ticketViewDto) {
        final long ticketId = ticketViewDto.getId();
        final Ticket ticket = ticketService.findById(ticketId);
        final UUID developerId = ticket.getDeveloperId();

        ticketService.unassignTicket(ticketId);

        emailService.send(
                ticketId,
                ticket.getTitle(),
                TicketStatus.OPEN.name(),
                userService.findUserById(developerId).getLogin(),
                userService.findUserById(ticket.getAuthorId()).getEmail()
        );

        LOGGER.info("Unassigned ticket: " + ticketId);
        return "redirect:/ticket/" + ticketId;
    }

    @PostMapping(value = {"/ticket/{id}"}, consumes = {"multipart/form-data"})
    public String uploadFile(@RequestParam(name = "file")
                                         final MultipartFile file, final TicketViewDto ticketViewDto) {
        final long ticketId = ticketViewDto.getId();
        final Ticket ticket = ticketService.findById(ticketId);
        final UUID developerId = ticket.getDeveloperId();

        if (!file.isEmpty()) {
            emailService.sendAttachment(
                    ticketId,
                    ticket.getTitle(),
                    userService.findUserById(developerId).getLogin(),
                    userService.findUserById(ticket.getAuthorId()).getEmail(),
                    file
            );
            LOGGER.info("File {} sent successfully", file.getOriginalFilename());
        } else {
            LOGGER.info("File not sent");
        }

        return "redirect:/ticket/" + ticketId;
    }

    @PostMapping(value = {"/ticket/{id}"}, params = "back")
    public String redirectHome() {
        return "redirect:/home";
    }
}
