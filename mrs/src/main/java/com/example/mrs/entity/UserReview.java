package com.example.mrs.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class UserReview {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Review review;

    @Column(name = "is_like")
    private boolean like;

    @Column(name = "is_dislike")
    private boolean dislike;
}
