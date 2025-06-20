package com.example.mrs.repository;

import com.example.mrs.entity.Review;
import com.example.mrs.entity.User;
import com.example.mrs.entity.UserReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserReviewRepository extends JpaRepository<UserReview, Long> {
    boolean existsByUserAndReview(User user, Review review);
    Optional<UserReview> findByUserAndReview(User user, Review review);
}
