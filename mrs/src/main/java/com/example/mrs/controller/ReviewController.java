package com.example.mrs.controller;

import com.example.mrs.dto.UserDTO;
import com.example.mrs.entity.Movie;
import com.example.mrs.entity.Review;
import com.example.mrs.entity.User;
import com.example.mrs.repository.MovieRepository;
import com.example.mrs.repository.ReviewRepository;
import com.example.mrs.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ReviewController {
    private final MovieRepository movieRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    //리뷰 작성, 수정
    @GetMapping("/movies/{movieId}/review")
    public String review(@PathVariable long movieId, HttpSession session, Model model) {
        Movie movie = movieRepository.findById(movieId).get();

        UserDTO userDTO = (UserDTO) session.getAttribute("user");
        User user = userRepository.findByUserId(userDTO.getUserId()).get();

        List<Review> reviewList = reviewRepository.findByMovieAndUser(movie, user);
        Review review = reviewList.isEmpty() ? null : reviewList.get(0);

        model.addAttribute("movie", movie);
        model.addAttribute("review", review);

        return "review-register";
    }

    @PostMapping("/movies/{movieId}/review")
    public String addReview(@PathVariable Long movieId, Review review, HttpSession session) {
        Movie movie = movieRepository.findById(movieId).get();

        UserDTO userDTO = (UserDTO) session.getAttribute("user");
        User user = userRepository.findByUserId(userDTO.getUserId()).get();

        review.setMovie(movie);
        review.setUser(user);

        reviewRepository.save(review);

        return "redirect:/movie-page/" + movieId;
    }

}






















