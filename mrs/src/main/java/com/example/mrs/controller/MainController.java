package com.example.mrs.controller;

import com.example.mrs.dto.UserDTO;
import jakarta.servlet.http.HttpSession;
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
    public String Main(HttpSession session, Model model) {

        // 로그인된 유저 세션 정보 모델로 넘기기
        Object user = session.getAttribute("user");
        if (user instanceof UserDTO dto) {
            model.addAttribute("sessionUser", dto);
        }

        List<Movie> movies = movieRepository.findAll();
        model.addAttribute("movie", movies);

        List<Review> reviews = reviewRepository.findAll();
        model.addAttribute("review", reviews);

        return "main";
    }

}
