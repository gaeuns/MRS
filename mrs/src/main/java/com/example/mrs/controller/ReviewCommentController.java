package com.example.mrs.controller;

import com.example.mrs.dto.ReviewCommentDTO;
import com.example.mrs.dto.UserDTO;
import com.example.mrs.entity.Movie;
import com.example.mrs.entity.Review;
import com.example.mrs.repository.ReviewRepository;
import com.example.mrs.repository.UserRepository;
import com.example.mrs.service.ReviewCommentService;
import com.example.mrs.entity.User;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class ReviewCommentController {

    private final ReviewCommentService reviewCommentService;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;

    //댓글 작성
    @PostMapping("/reviews/{id}/comments")
    public String createComment(@PathVariable Long id,
                                @ModelAttribute ReviewCommentDTO dto,
                                HttpSession session) {
        UserDTO userDTO = (UserDTO) session.getAttribute("user");
        User user = userRepository.findByUserId(userDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("유저 없음"));

        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 영화 없음"));

        dto.setId(id);
        reviewCommentService.createComment(dto, user);

        review.setCommentCount(review.getCommentCount() + 1);
        reviewRepository.save(review);

        return "redirect:/review-page/" + id;
    }

    //댓글 삭제
    @PostMapping("/review/{reviewId}/comment/{commentId}/delete")
    public String deleteComment(@PathVariable Long reviewId,
                                @PathVariable Long commentId,
                                HttpSession session) {

        UserDTO userDTO = (UserDTO) session.getAttribute("user");
        User user = userRepository.findByUserId(userDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("유저 없음"));

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("해당 영화 없음"));

        reviewCommentService.deleteComment(commentId, user);

        review.setCommentCount(review.getCommentCount() - 1);
        reviewRepository.save(review);

        return "redirect:/review-page/" + reviewId;
    }


    //댓글 수정
    @PostMapping("/reviews/{reviewId}/comments/{commentId}/edit")
    public String editComment(@PathVariable Long reviewId,
                              @PathVariable Long commentId,
                              @RequestParam String content,
                              HttpSession session) {
        UserDTO userDTO = (UserDTO) session.getAttribute("user");
        User user = userRepository.findByUserId(userDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("유저 없음"));

        reviewCommentService.updateComment(commentId, content, user);
        return "redirect:/review-page/" + reviewId;
    }

}