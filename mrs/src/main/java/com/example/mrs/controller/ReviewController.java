package com.example.mrs.controller;

import com.example.mrs.dto.ReviewCommentDTO;
import com.example.mrs.dto.UserDTO;
import com.example.mrs.entity.Movie;
import com.example.mrs.entity.Review;
import com.example.mrs.entity.User;
import com.example.mrs.entity.UserReview;
import com.example.mrs.repository.*;
import com.example.mrs.service.ReviewCommentService;
import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ReviewController {
    private final MovieRepository movieRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final UserReviewRepository userReviewRepository;
    private final ReviewCommentService reviewCommentService;

    //리뷰 작성, 수정
    @GetMapping("/movies/{movieId}/review")
    public String review(@PathVariable long movieId, HttpSession session, Model model) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new IllegalArgumentException("영화 정보를 찾을 수 없습니다."));
        model.addAttribute("movie", movie);

        UserDTO userDTO = (UserDTO) session.getAttribute("user");
        User user = userRepository.findByUserId(userDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("유저 정보를 찾을 수 없습니다."));

        Review review = reviewRepository.findFirstByMovieAndUser(movie, user).orElse(null);
        model.addAttribute("review", review);

        boolean hasReview = (review != null);
        model.addAttribute("hasReview", hasReview);

        model.addAttribute("movie", movie);
        model.addAttribute("review", review);

        return "review-register";
    }

    @PostMapping("/movies/{movieId}/review")
    public String addReview(@PathVariable Long movieId, Review review, HttpSession session) {
        // 1. 영화 엔티티 조회
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new IllegalArgumentException("해당 영화 없음"));

        // 2. 세션에서 로그인 유저 정보 꺼내기
        UserDTO userDTO = (UserDTO) session.getAttribute("user");
        User user = userRepository.findByUserId(userDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("유저 없음"));

        // 3. 리뷰에 유저와 영화 정보 set
        review.setUser(user);
        review.setMovie(movie);

        // 4. 리뷰 저장
        reviewRepository.save(review);

        // 5. 리뷰 저장 후 → 해당 영화 리뷰 카운트 증가, 평균 별점 계산 후 저장
        movie.setReviewCount(movie.getReviewCount() + 1);
        movie.setReviewSum(movie.getReviewSum() + review.getRating());
        movie.setAverageRating(movie.getReviewSum() / movie.getReviewCount());
        movieRepository.save(movie);

        return "redirect:/movie-page/" + movieId;
    }

    // 리뷰 추천 기능 (좋아요)
    @PostMapping("/review/{id}/like")
    public String toggleLike(@PathVariable Long id, HttpSession session,
                             @RequestHeader(value = "Referer", required = false) String referer) {
        // 1. 로그인 여부 확인
        UserDTO userDTO = (UserDTO) session.getAttribute("user");
        if (userDTO == null) return "redirect:/login";

        // 2. 사용자와 리뷰 조회
        User user = userRepository.findByUserId(userDTO.getUserId()).orElseThrow();
        Review review = reviewRepository.findById(id).orElseThrow();

        // 3. 기존에 해당 유저가 해당 리뷰에 대한 기록이 있는지 확인
        UserReview ur = userReviewRepository.findByUserAndReview(user, review).orElse(new UserReview());
        boolean likedBefore = ur.isLike();

        // 4. 상태 전환 처리
        if (likedBefore) {
            // 좋아요 → 취소
            ur.setLike(false);
            review.setLikeCount(review.getLikeCount() - 1);
        } else {
            // 싫어요 눌렀던 경우 → 취소
            if (ur.isDislike()) {
                ur.setDislike(false);
                review.setDislikeCount(review.getDislikeCount() - 1);
            }
            // 좋아요 추가
            ur.setLike(true);
            review.setLikeCount(review.getLikeCount() + 1);
        }

        // 5. 관계 설정 및 저장
        ur.setUser(user);
        ur.setReview(review);
        userReviewRepository.save(ur);
        reviewRepository.save(review);

        // 6. 원래 페이지로 리다이렉트
        return "redirect:" + (referer != null ? referer : "/");
    }

    // 리뷰 비추천 기능 (싫어요)
    @PostMapping("/review/{id}/dislike")
    public String toggleDislike(@PathVariable Long id, HttpSession session,
                                @RequestHeader(value = "Referer", required = false) String referer) {
        // 1. 로그인 여부 확인
        UserDTO userDTO = (UserDTO) session.getAttribute("user");
        if (userDTO == null) return "redirect:/login";

        // 2. 사용자와 리뷰 조회
        User user = userRepository.findByUserId(userDTO.getUserId()).orElseThrow();
        Review review = reviewRepository.findById(id).orElseThrow();

        // 3. 기존에 해당 유저가 해당 리뷰에 대한 기록이 있는지 확인
        UserReview ur = userReviewRepository.findByUserAndReview(user, review).orElse(new UserReview());
        boolean dislikedBefore = ur.isDislike();

        // 4. 상태 전환 처리
        if (dislikedBefore) {
            // 싫어요 → 취소
            ur.setDislike(false);
            review.setDislikeCount(review.getDislikeCount() - 1);
        } else {
            // 좋아요 눌렀던 경우 → 취소
            if (ur.isLike()) {
                ur.setLike(false);
                review.setLikeCount(review.getLikeCount() - 1);
            }
            // 싫어요 추가
            ur.setDislike(true);
            review.setDislikeCount(review.getDislikeCount() + 1);
        }

        // 5. 관계 설정 및 저장
        ur.setUser(user);
        ur.setReview(review);
        userReviewRepository.save(ur);
        reviewRepository.save(review);

        // 6. 원래 페이지로 리다이렉트
        return "redirect:" + (referer != null ? referer : "/");
    }

    //리뷰 상세
    @GetMapping("/review-page/{id}")
    public String reviewPage(@PathVariable long id, HttpSession session, Model model) {

        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 리뷰 없음"));
        model.addAttribute("review", review);

        //댓글 목록 추가
        List<ReviewCommentDTO> comments = reviewCommentService.getComments(id);
        model.addAttribute("comments", comments);

        if (session.getAttribute("user") != null) {
            UserDTO userDTO = (UserDTO) session.getAttribute("user");
            Optional<User> userOptional = userRepository.findByUserId(userDTO.getUserId());
            User user = userOptional.orElseThrow();

            boolean hasWrittenReview = reviewRepository.existsByUser(user);
            model.addAttribute("hasMyReview", hasWrittenReview);
        }

        if (session.getAttribute("user") != null) {
            UserDTO userDTO = (UserDTO) session.getAttribute("user");
            Optional<User> userOptional = userRepository.findByUserId(userDTO.getUserId());
            User user = userOptional.get();

            boolean hasWrittenReview = reviewRepository.existsByUser(user);
            model.addAttribute("hasMyReview", hasWrittenReview);

            //평가 여부 확인 (도움됨 / 안됨)
            Optional<UserReview> userReviewOpt = userReviewRepository.findByUserAndReview(user, review);
            boolean evaluatedLike = userReviewOpt.map(UserReview::isLike).orElse(false);
            boolean evaluatedDislike = userReviewOpt.map(UserReview::isDislike).orElse(false);

            model.addAttribute("evaluatedLikeByCurrentUser", evaluatedLike);
            model.addAttribute("evaluatedDislikeByCurrentUser", evaluatedDislike);
        }

        review.setViewCount(review.getViewCount() + 1);
        reviewRepository.save(review);

        return "review-page";
    }

    //리뷰 삭제
    @PostMapping("/movies/{movieId}/review/{reviewId}/delete")
    public String deleteReview(@PathVariable long movieId, @PathVariable long reviewId) {
        Movie movie = movieRepository.findById(movieId).orElseThrow();
        Review review = reviewRepository.findById(reviewId).orElseThrow();

        movie.setReviewCount(movie.getReviewCount() - 1);

        if (movie.getReviewCount() == 0) {
            movie.setAverageRating(0);
            movie.setReviewSum(0);
        } else {
            movie.setReviewSum(movie.getReviewSum() - review.getRating());
            movie.setAverageRating(movie.getReviewSum() / movie.getReviewCount());
        }

        movieRepository.save(movie);
        reviewRepository.delete(review);

        return "review-page";
    }

}