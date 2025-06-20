package com.example.mrs.repository;

import com.example.mrs.entity.Movie;
import com.example.mrs.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    Optional<Movie> findById(Long Id);
    Page<Movie> findByCategoryContaining(String category, Pageable pageable);
    Page<Movie> findByCategoryContainingAndTitleContaining(String category, String title, Pageable pageable);
    Page<Movie> findByTitleContaining(String title, Pageable pageable);

}
