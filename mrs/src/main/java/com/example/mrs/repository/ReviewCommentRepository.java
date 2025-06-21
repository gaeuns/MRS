package com.example.mrs.repository;

import com.example.mrs.entity.Review;
import com.example.mrs.entity.ReviewComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewCommentRepository extends JpaRepository<ReviewComment, Long> {
    List<ReviewComment> findByReviewOrderByCreatedAtAsc(Review review);
    List<ReviewComment> findByUser_UserId(String userId);
}
