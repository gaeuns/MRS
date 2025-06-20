package com.example.mrs.service;

import com.example.mrs.dto.ReviewCommentDTO;
import com.example.mrs.entity.Review;
import com.example.mrs.entity.ReviewComment;
import com.example.mrs.entity.User;
import com.example.mrs.repository.ReviewCommentRepository;
import com.example.mrs.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewCommentService {

    private final ReviewRepository reviewRepository;
    private final ReviewCommentRepository commentRepository;

    public void createComment(ReviewCommentDTO dto, User user) {
        Review review = reviewRepository.findById(dto.getId()).orElseThrow();

        ReviewComment comment = new ReviewComment();
        comment.setReview(review);
        comment.setUser(user);
        comment.setContent(dto.getContent());
        comment.setCreatedAt(java.time.LocalDateTime.now());

        commentRepository.save(comment);
    }

    public List<ReviewCommentDTO> getComments(Long reviewId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow();

        return commentRepository.findByReviewOrderByCreatedAtAsc(review).stream()
                .map(comment -> {
                    ReviewCommentDTO dto = new ReviewCommentDTO();
                    dto.setId(comment.getId());
                    dto.setUserName(comment.getUser().getUserName());
                    dto.setContent(comment.getContent());
                    dto.setCreatedAt(comment.getCreatedAt());
                    dto.setAuthorId(comment.getUser().getId());
                    dto.setAuthorUserId(comment.getUser().getUserId());
                    return dto;
                })
                .toList();
    }

    public void deleteComment(Long commentId, User user) {
        ReviewComment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글 없음"));
        if (!comment.getUser().equals(user)) {
            throw new SecurityException("본인만 삭제 가능");
        }
        commentRepository.delete(comment);
    }

    public void updateComment(Long commentId, String content, User user) {
        ReviewComment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글 없음"));
        if (!comment.getUser().equals(user)) {
            throw new SecurityException("본인만 수정 가능");
        }
        comment.setContent(content);
        comment.setCreatedAt(LocalDateTime.now());
        commentRepository.save(comment);
    }

}
