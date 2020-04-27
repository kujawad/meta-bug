package com.metabug.service;

import com.metabug.persistence.dao.TicketRepository;
import com.metabug.persistence.model.Ticket;
import com.metabug.web.dto.TicketDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
public class TicketService {
    private TicketRepository ticketRepository;
    private UserService userService;

    @Autowired
    public TicketService(final TicketRepository ticketRepository,
                         final UserService userService) {
        this.ticketRepository = ticketRepository;
        this.userService = userService;
    }

    public List<Ticket> findAll() {
        return ticketRepository.findAll();
    }

    public void save(final TicketDto ticketDto, final Principal principal) {
        final Ticket ticket = new Ticket();
        ticket.setTitle(ticketDto.getTitle());
        ticket.setDescription(ticketDto.getDescription());

        //TODO: check if users exists? just in case?
        ticket.setAuthorId(userService.findUserByLogin(principal.getName()).getId());

        ticketRepository.save(ticket);
    }
}
