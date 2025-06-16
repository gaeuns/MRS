package com.example.mrs.controller;

import com.example.mrs.entity.Movie;
import com.example.mrs.entity.Review;
import com.example.mrs.repository.MovieRepository;
import com.example.mrs.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final MovieRepository movieRepository;
    private final ReviewRepository reviewRepository;

    @GetMapping("/")
    public String Main(Model model) {
        List<Movie> movies = movieRepository.findAll();
        model.addAttribute("movie", movies);

        List<Review> reviews = reviewRepository.findAll();
        model.addAttribute("review", reviews);

        return "main";
    }

}
