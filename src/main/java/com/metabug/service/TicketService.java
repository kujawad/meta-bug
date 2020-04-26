package com.metabug.service;

import com.metabug.persistence.dao.TicketRepository;
import com.metabug.persistence.model.Ticket;
import com.metabug.web.dto.TicketDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public Ticket findUserByEmail(final String title) {
        return ticketRepository.findByTitle(title);
    }

    public void save(final TicketDto ticketDto) {
        final Ticket ticket = new Ticket();
        ticket.setTitle(ticketDto.getTitle());
        ticket.setDescription(ticketDto.getDescription());

        final String author = ticketDto.getAuthor();

        if (userService.findUserByLogin(author) != null) {
            ticket.setAuthorId(userService.findUserByLogin(author).getId());
        } else {
            ticket.setAuthorId(userService.findUserByEmail(author).getId());
        }

        ticketRepository.save(ticket);
    }
}
