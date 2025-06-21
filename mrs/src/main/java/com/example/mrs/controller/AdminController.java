package com.example.mrs.controller;

import com.example.mrs.dto.UserDTO;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller  // 스프링 MVC 컨트롤러로 등록
public class AdminController {

    // 관리자 페이지 접근 요청
    @GetMapping("/admin")
    public String adminPage(HttpSession session, Model model) {
        // 현재 로그인한 사용자 세션에서 user 객체 조회
        Object user = session.getAttribute("user");

        // 로그인 되어 있는지 확인하고, 관리자인지 확인
        // 만약 user가 UserDTO 객체가 아니거나 ADMIN 권한이 아니면 일반 유저 → 메인 페이지로 리다이렉트
        if (!(user instanceof UserDTO dto) || !"ADMIN".equals(dto.getUserRole().name())) {
            return "redirect:/";
        }

        // 관리자라면 현재 로그인한 사용자 정보 sessionUser로 모델에 전달 (템플릿에서 활용 가능)
        model.addAttribute("sessionUser", dto);

        // 관리자 메인 페이지(adminmain.html) 뷰 반환
        return "admin/adminmain";
    }

}
