package com.example.mrs.controller;

import com.example.mrs.dto.UserDTO;
import com.example.mrs.entity.Notice;
import com.example.mrs.repository.NoticeRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
<<<<<<< HEAD
import org.springframework.web.multipart.MultipartFile;
=======

import java.util.List;
import java.util.Optional;
>>>>>>> ef1b919951c19ce97bbfeaffacf47abcde7169ae

import java.io.File;
import java.io.IOException;
import java.util.*;

@Controller  // 스프링 MVC 컨트롤러 등록
@RequiredArgsConstructor  // 생성자 주입 사용 (noticeRepository 자동 주입)
@RequestMapping("/notice")  // 공지사항 URL 시작 경로
public class NoticeController {

    // 공지사항 레포지토리 의존성 주입
    private final NoticeRepository noticeRepository;

<<<<<<< HEAD
    // 공지사항 목록 페이지
    @GetMapping
    public String noticeList(Model model,
                             @RequestParam(required = false) String importance,  // 중요도 필터
                             @RequestParam(required = false) String keyword,     // 검색어
                             @RequestParam(required = false) String sort,        // 정렬 기준
                             @RequestParam(defaultValue = "0") int page,         // 페이지 번호
                             @RequestParam(defaultValue = "10") int size) {      // 페이지 크기

        // 정렬 방식 결정
        Sort sorting = Sort.unsorted();
        if ("latest".equals(sort)) {
            sorting = Sort.by(Sort.Direction.DESC, "createdAt");  // 최신순
        } else if ("views".equals(sort)) {
            sorting = Sort.by(Sort.Direction.DESC, "viewCount");  // 조회수순
        } else if ("title".equals(sort)) {
            sorting = Sort.by(Sort.Direction.ASC, "title");        // 제목순
        }

        Pageable pageable = PageRequest.of(page, size, sorting);
        Page<Notice> noticePage;

        // 상단 중요 공지 분리 조건: 검색X, 정렬X, 필터X 상태일 때만 표시
        boolean showImportantSeparately = (sort == null || sort.isBlank())
                && (importance == null || importance.isBlank())
                && (keyword == null || keyword.isBlank());

        List<Notice> importantNotices = new ArrayList<>();
        if (showImportantSeparately) {
            importantNotices = noticeRepository.findByImportance("IMPORTANT");
        }

        // 검색어가 있을 경우 검색 우선 적용
        if (keyword != null && !keyword.isBlank()) {
            if ("IMPORTANT".equals(importance)) {
                noticePage = noticeRepository.findByImportanceAndKeyword("IMPORTANT", keyword, pageable);
            } else if ("NORMAL".equals(importance)) {
                noticePage = noticeRepository.findByImportanceAndKeyword("NORMAL", keyword, pageable);
            } else {
                noticePage = noticeRepository.findByKeyword(keyword, pageable);
            }
        } else {
            // 검색어 없을 경우 중요도와 정렬만 적용
            if ("IMPORTANT".equals(importance)) {
                noticePage = noticeRepository.findByImportance("IMPORTANT", pageable);
            } else if ("NORMAL".equals(importance)) {
                noticePage = noticeRepository.findByImportance("NORMAL", pageable);
            } else if (showImportantSeparately) {
                noticePage = noticeRepository.findByImportance("NORMAL", pageable);
            } else {
                noticePage = noticeRepository.findAll(pageable);
            }
        }

        // 모델에 데이터 전달 (리스트, 페이지 정보, 중요공지 리스트)
        model.addAttribute("notices", noticePage.getContent());
        model.addAttribute("noticePage", noticePage);
        model.addAttribute("importantNotices", importantNotices);

        // 검색/필터 파라미터 유지용
        Map<String, Object> param = new HashMap<>();
        param.put("importance", importance);
        param.put("keyword", keyword);
        param.put("sort", sort);
        model.addAttribute("param", param);
=======
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
>>>>>>> ef1b919951c19ce97bbfeaffacf47abcde7169ae

        return "notice/list";
    }

<<<<<<< HEAD
    // 공지사항 작성 폼
    @GetMapping("/write")
    public String showWriteForm(Model model, HttpSession session) {
        // 관리자 권한 확인
        Object user = session.getAttribute("user");
        if (!(user instanceof UserDTO dto) || !"ADMIN".equals(dto.getUserRole().name())) {
            return "redirect:/notice";  // 일반 유저 접근 제한
        }
        model.addAttribute("notice", new Notice());  // 빈 폼 객체 전달
        return "notice/noticewrite";
    }

    // 공지사항 작성 처리
    @PostMapping("/write")
    public String submitNotice(@ModelAttribute Notice notice,
                               @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                               HttpSession session) throws IOException {

        // 이미지 첨부가 있으면 서버 로컬에 저장
        if (imageFile != null && !imageFile.isEmpty()) {
            String rootPath = System.getProperty("user.dir");
            String uploadDir = rootPath + "/uploads/";
            String fileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
            File file = new File(uploadDir + fileName);
            file.getParentFile().mkdirs();  // 디렉토리 생성
            imageFile.transferTo(file);
            notice.setImagePath("/uploads/" + fileName);  // 경로 저장
        }

        // 작성자 세션에서 설정
=======
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
>>>>>>> ef1b919951c19ce97bbfeaffacf47abcde7169ae
        Object user = session.getAttribute("user");
        if (user instanceof UserDTO dto) {
            notice.setAuthor(dto.getUserName());
        }
<<<<<<< HEAD

        // DB 저장
=======
>>>>>>> ef1b919951c19ce97bbfeaffacf47abcde7169ae
        noticeRepository.save(notice);
        return "redirect:/notice";
    }

<<<<<<< HEAD
    // 공지사항 상세 보기
=======
    //  공지 상세 보기
>>>>>>> ef1b919951c19ce97bbfeaffacf47abcde7169ae
    @GetMapping("/{id}")
    public String viewNotice(@PathVariable Long id, Model model, HttpSession session) {
        Optional<Notice> noticeOpt = noticeRepository.findById(id);
        if (noticeOpt.isPresent()) {
            Notice notice = noticeOpt.get();
<<<<<<< HEAD

            // 조회수 1 증가
            notice.setViewCount(notice.getViewCount() + 1);
            noticeRepository.save(notice);

            model.addAttribute("notice", notice);

            // 로그인한 사용자 정보 전달 (관리자 버튼 제어에 활용)
=======
            notice.setViewCount(notice.getViewCount() + 1);
            noticeRepository.save(notice);
            model.addAttribute("notice", notice);

            //  sessionUser 모델에 담기
>>>>>>> ef1b919951c19ce97bbfeaffacf47abcde7169ae
            Object user = session.getAttribute("user");
            if (user instanceof UserDTO dto) {
                model.addAttribute("sessionUser", dto);
            }

            return "notice/noticeview";
        } else {
            return "redirect:/notice";
        }
    }

<<<<<<< HEAD
    // 공지사항 수정 폼
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model, HttpSession session) {
        // 관리자 권한 확인
=======
    //  공지 수정 폼
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model, HttpSession session) {
>>>>>>> ef1b919951c19ce97bbfeaffacf47abcde7169ae
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

<<<<<<< HEAD
    // 공지사항 수정 처리
    @PostMapping("/edit/{id}")
    public String updateNotice(@PathVariable Long id,
                               @ModelAttribute Notice updatedNotice,
                               @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                               HttpSession session) throws IOException {

        // 관리자 권한 확인
=======
    //  공지 수정 처리
    @PostMapping("/edit/{id}")
    public String updateNotice(@PathVariable Long id,
                               @ModelAttribute Notice updatedNotice,
                               HttpSession session) {
>>>>>>> ef1b919951c19ce97bbfeaffacf47abcde7169ae
        Object user = session.getAttribute("user");
        if (!(user instanceof UserDTO dto) || !"ADMIN".equals(dto.getUserRole().name())) {
            return "redirect:/notice";
        }

        Optional<Notice> noticeOpt = noticeRepository.findById(id);
        if (noticeOpt.isPresent()) {
            Notice notice = noticeOpt.get();
<<<<<<< HEAD

            // 수정 항목 반영
            notice.setTitle(updatedNotice.getTitle());
            notice.setContent(updatedNotice.getContent());
            notice.setImportance(updatedNotice.getImportance());

            // 이미지 교체가 있을 경우 파일 새로 저장
            if (imageFile != null && !imageFile.isEmpty()) {
                String rootPath = System.getProperty("user.dir");
                String uploadDir = rootPath + "/uploads/";
                String fileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
                File file = new File(uploadDir + fileName);
                file.getParentFile().mkdirs();
                imageFile.transferTo(file);
                notice.setImagePath("/uploads/" + fileName);
            }

=======
            notice.setTitle(updatedNotice.getTitle());
            notice.setContent(updatedNotice.getContent());
            notice.setImportance(updatedNotice.getImportance());
>>>>>>> ef1b919951c19ce97bbfeaffacf47abcde7169ae
            noticeRepository.save(notice);
        }

        return "redirect:/notice/" + id;
    }

<<<<<<< HEAD
    // 공지사항 삭제 처리
    @PostMapping("/delete/{id}")
    public String deleteNotice(@PathVariable Long id, HttpSession session) {
        // 관리자 권한 확인
=======
    //  공지 삭제 처리
    @PostMapping("/delete/{id}")
    public String deleteNotice(@PathVariable Long id, HttpSession session) {
>>>>>>> ef1b919951c19ce97bbfeaffacf47abcde7169ae
        Object user = session.getAttribute("user");
        if (!(user instanceof UserDTO dto) || !"ADMIN".equals(dto.getUserRole().name())) {
            return "redirect:/notice";
        }

        noticeRepository.deleteById(id);
        return "redirect:/notice";
    }
<<<<<<< HEAD
}
=======
}
>>>>>>> ef1b919951c19ce97bbfeaffacf47abcde7169ae
