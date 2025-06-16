package com.example.mrs.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Entity
@Data
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;

    @Column(columnDefinition = "TEXT")
    private String synopsis;
    private String releaseDate;
    private String director;
    private String actor;
    private String posterUrl;

    @Transient
    private MultipartFile posterFile;
    private String trailerUrl;
    private String category;
    private String averageRating;
    private String reviewCount;
    private String runtime;
    private String country;
    private String rating;
}
