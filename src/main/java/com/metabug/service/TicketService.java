package com.metabug.service;

import com.metabug.persistence.dao.TicketRepository;
import com.metabug.persistence.model.Ticket;
import com.metabug.persistence.model.TicketStatus;
import com.metabug.persistence.model.User;
import com.metabug.web.dto.TicketDto;
import com.metabug.web.dto.TicketViewDto;
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

    public Ticket findById(final long id) {
        return ticketRepository.findById(id);
    }

    public void save(final TicketDto ticketDto, final Principal principal) {
        final Ticket ticket = new Ticket();
        ticket.setTitle(ticketDto.getTitle());
        ticket.setDescription(ticketDto.getDescription());
        ticket.setStatus(TicketStatus.OPEN);

        ticket.setAuthorId(userService.findUserByLogin(principal.getName()).getId());

        ticketRepository.save(ticket);
    }

    public void assignTicket(final long id, final String login) {
        final Ticket ticket = ticketRepository.findById(id);
        final User user = userService.findUserByLogin(login);
        ticket.setDeveloperId(user.getId());
        ticket.setStatus(TicketStatus.ONGOING);
    }

    public void markTicketDone(final long id) {
        final Ticket ticket = ticketRepository.findById(id);
        ticket.setStatus(TicketStatus.DONE);
    }

    public TicketViewDto toTicketView(final Ticket ticket) {
        final TicketViewDto ticketViewDto = new TicketViewDto();
        final String author = userService.findUserById(ticket.getAuthorId()).getLogin();
        String developer = null;
        if(ticket.getDeveloperId() != null) {
            developer = userService.findUserById(ticket.getDeveloperId()).getLogin();
        }
        ticketViewDto.setId(ticket.getId());
        ticketViewDto.setAuthor(author);
        ticketViewDto.setDeveloper(developer);
        ticketViewDto.setTitle(ticket.getTitle());
        ticketViewDto.setDescription(ticket.getDescription());
        ticketViewDto.setStatus(ticket.getStatus());
        return ticketViewDto;
    }
}
