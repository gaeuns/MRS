package com.example.mrs.repository;

import com.example.mrs.dto.UserDTO;
import com.example.mrs.entity.Movie;
import com.example.mrs.entity.Review;
import com.example.mrs.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByMovie(Movie movie);

    boolean existsByUser(User user);
    boolean existsByMovieAndUser(Movie movie, User user);

    Optional<Review> findFirstByMovieAndUser(Movie movie, User user);

}
