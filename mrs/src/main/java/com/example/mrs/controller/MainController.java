package com.example.mrs.controller;

import com.example.mrs.dto.UserDTO;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String Main(HttpSession session, Model model) {

        // 로그인된 유저 세션 정보 모델로 넘기기
        Object user = session.getAttribute("user");
        if (user instanceof UserDTO dto) {
            model.addAttribute("sessionUser", dto);
        }

        return "main";
    }

}
