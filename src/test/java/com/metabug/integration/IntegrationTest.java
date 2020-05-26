package com.metabug.integration;

import com.metabug.MetaBugApplication;
import com.metabug.integration.configuration.H2TestProfileJPAConfig;
import com.metabug.persistence.dao.TicketRepository;
import com.metabug.persistence.model.Ticket;
import com.metabug.persistence.model.TicketStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.UUID;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
        H2TestProfileJPAConfig.class,
        MetaBugApplication.class
})
@SpringBootTest(properties = {
        "spring.profiles.active=test",
        "test-url=a"
})
@ActiveProfiles(profiles = "test")
public class IntegrationTest {

    @Autowired
    private TicketRepository ticketRepository;

    @Test
    public void shouldSaveTicket() {
        // given
        final int id = 1;
        final String title = "title";
        final String description = "description";
        final UUID authorId = UUID.randomUUID();
        final UUID developerId = UUID.randomUUID();
        final TicketStatus status = TicketStatus.OPEN;

        final Ticket ticket = new Ticket(
                id, title, description, authorId, developerId, status
        );

        // when
        ticketRepository.save(ticket);

        // then
        final Ticket actual = ticketRepository.findById(id);
        assertEquals(ticket, actual);
    }
}
