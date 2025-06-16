package com.example.mrs.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Review {
    @Id
    @GeneratedValue
    private int id;
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne
    private User user;
    private int rating;

    @Column(name = "date", columnDefinition = "DATETIME")
    private LocalDateTime date;
    private boolean hasSpoiler;

    @ManyToOne
    private Movie movie;

    @PrePersist
    protected void onCreate() {
        this.date = LocalDateTime.now();
    }
}
