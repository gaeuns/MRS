package com.example.mrs.controller;

import com.example.mrs.entity.Notice;
import com.example.mrs.repository.NoticeRepository;
import com.example.mrs.dto.UserDTO;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/notice")
public class NoticeController {

    private final NoticeRepository noticeRepository;

    @GetMapping
    public String noticeList(Model model, @PageableDefault(size = 10) Pageable pageable, HttpSession session) {
        UserDTO user = (UserDTO) session.getAttribute("user");

        if (user == null) {
            return "redirect:/login"; // 로그인 안 했으면 로그인 페이지로
        }

        Page<Notice> noticePage = noticeRepository.findAll(pageable);
        model.addAttribute("notices", noticePage.getContent());
        model.addAttribute("noticePage", noticePage);
        return "notice/list";
    }
}
