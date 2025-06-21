package com.example.mrs.controller;

import com.example.mrs.entity.Movie;
import com.example.mrs.entity.Review;
import com.example.mrs.entity.User;
import com.example.mrs.repository.MovieRepository;
import com.example.mrs.repository.ReviewRepository;
import com.example.mrs.repository.UserRepository;
import com.example.mrs.dto.UserDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;
    private final MovieRepository movieRepository;
    private final ReviewRepository reviewRepository;

    //회원가입
    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("userInfo", new User());
        return "signup";
    }

    @PostMapping("/adduser")
    public String addUser(@ModelAttribute("userInfo") User user, BindingResult result, Model model) {

        if (userRepository.findByUserId(user.getUserId()).isPresent()) {
            result.rejectValue("userId", "error.userId", "이미 사용 중인 ID입니다.");
            return "signup";
        }

        if (!user.getUserPassword().equals(user.getConfirmPassword())) {
            result.rejectValue("confirmPassword", "error.confirmPassword", "비밀번호가 일치하지 않습니다.");
            return "signup";
        }

        userRepository.save(user);

        model.addAttribute("success", true);
        return "login";
    }

    //로그인
    @GetMapping("/login")
    public String login(HttpServletRequest request, HttpSession session, Model model) {
        String referer = request.getHeader("Referer");
        if (referer != null && !referer.contains("/login")) {
            session.setAttribute("prevPage", referer);
        }

        model.addAttribute("userInfo", new User());

        return "login";
    }

    @PostMapping("/login")
    public String postMain(@ModelAttribute("userInfo") User user, BindingResult result, HttpSession session, Model model) {
        var dbUser = userRepository.findByUserId(user.getUserId()).orElse(null);

        if (dbUser == null) {
            result.rejectValue("userId", "error.userId", "존재하지 않는 ID입니당당당.");
            return "login";
        }

        if (!Objects.equals(user.getUserPassword(), dbUser.getUserPassword())) {
            result.rejectValue("userPassword", "error.userPassword", "잘못된 비밀번호입니다.");
            return "login";
        }
        session.setAttribute("user", UserDTO.fromEntity(dbUser));

        List<Movie> movies = movieRepository.findAll();
        model.addAttribute("movie", movies);

        List<Review> reviews = reviewRepository.findAll();
        model.addAttribute("review", reviews);

        String redirectUrl = (String) session.getAttribute("prevPage");
        return "redirect:" + (redirectUrl != null ? redirectUrl : "/");
    }

    //로그아웃
    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/";
    }

    //마이페이지

}
