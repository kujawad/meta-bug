package com.metabug.web.dto;

import com.metabug.persistence.model.TicketStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TicketViewDto {
    private long id;
    private String author;
    private String developer;
    private String title;
    private String description;
    private TicketStatus status;
}
