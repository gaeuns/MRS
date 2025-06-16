package com.example.mrs.controller;

import com.example.mrs.dto.UserDTO;
import com.example.mrs.entity.Notice;
import com.example.mrs.repository.NoticeRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/notice")
public class NoticeController {

    private final NoticeRepository noticeRepository;

    //  공지사항 목록 페이지
    @GetMapping
    public String noticeList(Model model,
                             @PageableDefault(size = 10) Pageable pageable,
                             @RequestParam(required = false) String importance) {

        Page<Notice> noticePage;

        if (importance != null) {
            noticePage = noticeRepository.findByImportance(importance, pageable);
            model.addAttribute("importantNotices", List.of());
        } else {
            List<Notice> importantNotices = noticeRepository.findByImportance("IMPORTANT");
            noticePage = noticeRepository.findByImportance("NORMAL", pageable);
            model.addAttribute("importantNotices", importantNotices);
        }

        model.addAttribute("notices", noticePage.getContent());
        model.addAttribute("noticePage", noticePage);
        model.addAttribute("param", importance);

        return "notice/list";
    }

    //  공지 작성 폼
    @GetMapping("/write")
    public String showWriteForm(Model model, HttpSession session) {
        Object user = session.getAttribute("user");
        if (!(user instanceof UserDTO dto) || !"ADMIN".equals(dto.getUserRole().name())) {
            return "redirect:/notice";
        }
        model.addAttribute("notice", new Notice());
        return "notice/noticewrite";
    }

    //  공지 작성 처리
    @PostMapping("/write")
    public String submitNotice(@ModelAttribute Notice notice, HttpSession session) {
        Object user = session.getAttribute("user");
        if (user instanceof UserDTO dto) {
            notice.setAuthor(dto.getUserName());
        }
        noticeRepository.save(notice);
        return "redirect:/notice";
    }

    //  공지 상세 보기
    @GetMapping("/{id}")
    public String viewNotice(@PathVariable Long id, Model model, HttpSession session) {
        Optional<Notice> noticeOpt = noticeRepository.findById(id);
        if (noticeOpt.isPresent()) {
            Notice notice = noticeOpt.get();
            notice.setViewCount(notice.getViewCount() + 1);
            noticeRepository.save(notice);
            model.addAttribute("notice", notice);

            //  sessionUser 모델에 담기
            Object user = session.getAttribute("user");
            if (user instanceof UserDTO dto) {
                model.addAttribute("sessionUser", dto);
            }

            return "notice/noticeview";
        } else {
            return "redirect:/notice";
        }
    }

    //  공지 수정 폼
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model, HttpSession session) {
        Object user = session.getAttribute("user");
        if (!(user instanceof UserDTO dto) || !"ADMIN".equals(dto.getUserRole().name())) {
            return "redirect:/notice";
        }

        Optional<Notice> noticeOpt = noticeRepository.findById(id);
        if (noticeOpt.isPresent()) {
            model.addAttribute("notice", noticeOpt.get());
            return "notice/noticeedit";
        } else {
            return "redirect:/notice";
        }
    }

    //  공지 수정 처리
    @PostMapping("/edit/{id}")
    public String updateNotice(@PathVariable Long id,
                               @ModelAttribute Notice updatedNotice,
                               HttpSession session) {
        Object user = session.getAttribute("user");
        if (!(user instanceof UserDTO dto) || !"ADMIN".equals(dto.getUserRole().name())) {
            return "redirect:/notice";
        }

        Optional<Notice> noticeOpt = noticeRepository.findById(id);
        if (noticeOpt.isPresent()) {
            Notice notice = noticeOpt.get();
            notice.setTitle(updatedNotice.getTitle());
            notice.setContent(updatedNotice.getContent());
            notice.setImportance(updatedNotice.getImportance());
            noticeRepository.save(notice);
        }

        return "redirect:/notice/" + id;
    }

    //  공지 삭제 처리
    @PostMapping("/delete/{id}")
    public String deleteNotice(@PathVariable Long id, HttpSession session) {
        Object user = session.getAttribute("user");
        if (!(user instanceof UserDTO dto) || !"ADMIN".equals(dto.getUserRole().name())) {
            return "redirect:/notice";
        }

        noticeRepository.deleteById(id);
        return "redirect:/notice";
    }
}