package com.metabug.persistence.dao;

import com.metabug.persistence.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, UUID> {
    Ticket findByTitle(final String title);
}
