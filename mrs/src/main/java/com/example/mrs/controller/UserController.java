package com.example.mrs.controller;

import com.example.mrs.entity.User;
import com.example.mrs.entity.UserRole;
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

import java.util.Objects;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;

    // 회원가입
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

        // 기본 사용자 권한 부여
        user.setUserRole(UserRole.USER);

        userRepository.save(user);

        model.addAttribute("success", true);
        return "login";
    }

    // 로그인
    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("userInfo", new User());
        return "login";
    }

    @PostMapping("/login")
    public String postMain(@ModelAttribute("userInfo") User user, BindingResult result, HttpSession session) {
        var dbUser = userRepository.findByUserId(user.getUserId()).orElse(null);

        if (dbUser == null) {
            result.rejectValue("userId", "error.userId", "존재하지 않는 ID입니당당당.");
            return "login";
        }

        if (!Objects.equals(user.getUserPassword(), dbUser.getUserPassword())) {
            result.rejectValue("userPassword", "error.userPassword", "잘못된 비밀번호입니다.");
            return "login";
        }

        //  로그인 시 userRole 포함된 DTO로 세션 저장
        session.setAttribute("user", UserDTO.fromEntity(dbUser));
        return "main";
    }

    // 로그아웃
    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "main";
    }


}
