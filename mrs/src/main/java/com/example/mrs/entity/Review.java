package com.example.mrs.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Review {
    @Id
    @GeneratedValue
    private long id;
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
    private int rating;

    private LocalDateTime createdAt = LocalDateTime.now();
    private boolean hasSpoiler;

    @ManyToOne(fetch = FetchType.LAZY)
    private Movie movie;
    private int likeCount;
    private int viewCount;
    private int commentCount;
    private int dislikeCount;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserReview> userReviews = new ArrayList<>();

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewComment> reviewComments = new ArrayList<>();
}
