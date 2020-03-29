package com.metabug.service;

import com.metabug.persistence.model.Ticket;
import com.metabug.persistence.dao.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TicketService {
    private TicketRepository ticketRepository;

    @Autowired
    public TicketService(final TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public Ticket findUserByEmail(final String title) {
        return ticketRepository.findByTitle(title);
    }

    public Ticket save(final Ticket ticket) {
        return ticketRepository.save(ticket);
    }
}
