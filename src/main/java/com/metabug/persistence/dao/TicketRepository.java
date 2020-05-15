package com.metabug.persistence.dao;

import com.metabug.persistence.model.Ticket;
import com.metabug.persistence.model.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, UUID> {
    List<Ticket> findAllByStatus(final TicketStatus status);

    List<Ticket> findAllByDeveloperIdAndStatus(final UUID developerId, final TicketStatus status);

    List<Ticket> findAll();

    Ticket findById(final long id);
}
