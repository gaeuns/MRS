package com.example.mrs.controller;

import com.example.mrs.entity.Movie;
import com.example.mrs.entity.Review;
import com.example.mrs.repository.MovieRepository;
import com.example.mrs.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Controller
@RequiredArgsConstructor
public class MovieController {
    private final MovieRepository movieRepository;
    private final ReviewRepository reviewRepository;

    //영화 목록
    @GetMapping("/movies")
    public String movies(@RequestParam(required = false) String category,
                         @RequestParam(required = false) String sort,
                         @RequestParam(required = false) String keyword,
                         @RequestParam(defaultValue = "0") int page,
                         Model model) {

        Pageable pageable = switch (sort != null ? sort : "") {
            case "latest" -> PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "releaseDate"));
            case "rating" -> PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "averageRating"));
            case "popular" -> PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "reviewCount"));
            case "title" -> PageRequest.of(page, 10, Sort.by(Sort.Direction.ASC, "title"));
            default -> PageRequest.of(page, 10);
        };

        Page<Movie> moviePage;

        if (category != null && !category.isEmpty() && keyword != null && !keyword.isEmpty()) {
            moviePage = movieRepository.findByCategoryContainingAndTitleContaining(category, keyword, pageable);
        } else if (category != null && !category.isEmpty()) {
            moviePage = movieRepository.findByCategoryContaining(category, pageable);
        } else if (keyword != null && !keyword.isEmpty()) {
            moviePage = movieRepository.findByTitleContaining(keyword, pageable);
        } else {
            moviePage = movieRepository.findAll(pageable);
        }

        model.addAttribute("movies", moviePage.getContent());
        model.addAttribute("moviePage", moviePage);

        Map<String, String> param = new HashMap<>();
        param.put("category", category != null ? category : "");
        param.put("sort", sort != null ? sort : "");
        param.put("keyword", keyword != null ? keyword : "");
        model.addAttribute("param", param);

        return "movies";
    }

    //영화 등록
    @GetMapping("/movie-register")
    public String movieRegister(Model model) {
        model.addAttribute("movies", new Movie());

        return "movie-register";
    }

    @SneakyThrows
    @PostMapping("/addmovie")
    public String addMovie(@ModelAttribute("movies") Movie movie, @RequestParam("posterFile") MultipartFile posterFile,
                           Model model) throws IOException {

        String uploadDir = new File("src/main/resources/static/uploads").getAbsolutePath();
        String fileName = UUID.randomUUID() + "_" + posterFile.getOriginalFilename();
        File destination = new File(uploadDir, fileName);
        posterFile.transferTo(destination);

        movie.setPosterUrl("/uploads/" + fileName);

        movieRepository.save(movie);
        model.addAttribute("success", true);

        return "movies";
    }

    //영화 상세
    @GetMapping("/movie-page/{id}")
    public String moviePage(@PathVariable long id, Model model) {
        Movie movie = movieRepository.findById(id).get();
        model.addAttribute("movie", movie);

        List<Review> review = reviewRepository.findByMovie(movie);
        model.addAttribute("review", review);

        return "movie-page";
    }

    // 리뷰 수정
    /*@PostMapping("/movie-page/{movieId}/review/{reviewId}/edit")
    public String updateReview(@PathVariable Long movieId,
                               @PathVariable Long reviewId,
                               Review reviewData) {
        Review review = reviewRepository.findById(Math.toIntExact(reviewId))
                .orElseThrow(() -> new IllegalArgumentException("리뷰가 존재하지 않습니다."));
        review.setRating(reviewData.getRating());
        review.setDescription(reviewData.getDescription());
        review.setHasSpoiler(reviewData.isHasSpoiler());
        reviewRepository.save(review);
        return "redirect:/movie-page/" + movieId;
    }*/


}
