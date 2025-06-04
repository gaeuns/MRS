package com.example.mrs.Controller;

import com.example.mrs.Entity.User;
import com.example.mrs.Repository.UserRepository;
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
public class MemberController {
    private final UserRepository userRepository;

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

        user.setUserPassword(user.getUserPassword());
        userRepository.save(user);

        model.addAttribute("success", true);
        return "login";
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("userInfo", new User());
        return "login";
    }

    @PostMapping("/login")
    public String postMain(@ModelAttribute("userInfo") User user, BindingResult result, HttpSession session) {
        if (userRepository.findByUserId(user.getUserId()).isEmpty()) {
            result.rejectValue("userId", "error.userId", "존재하지 않는 ID입니당당당.");
            return "login";
        }

        var dbUser = userRepository.findByUserId(user.getUserId()).get();

        // 🔐 비밀번호 비교 (암호화된 비밀번호와 입력값 비교)
        if (!Objects.equals(user.getUserPassword(), dbUser.getUserPassword())) {
            result.rejectValue("userPassword", "error.userPassword", "잘못된 비밀번호입니다.");
            return "login";
        }
        System.out.println("여깅1");
        session.setAttribute("user", dbUser);
        System.out.println("여깅2");
        return "main";
    }

    @GetMapping("/main")
    public String mainPage() {
        return "main";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "main";
    }


}
