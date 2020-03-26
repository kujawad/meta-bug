package com.metabug.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import java.util.UUID;

@Entity
@Getter
@Setter
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String title;
    private String description;
    private UUID authorId;
    private UUID developerId;


    protected Ticket() {
    }

    public Ticket(final long id, final String title, final String description, final UUID authorId, final UUID developerId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.authorId = authorId;
        this.developerId = developerId;
    }

}