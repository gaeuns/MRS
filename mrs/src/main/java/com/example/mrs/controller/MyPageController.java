package com.example.mrs.controller;

import com.example.mrs.dto.ReviewCommentDTO;
import com.example.mrs.dto.UserDTO;
import com.example.mrs.dto.UserUpdateDTO;
import com.example.mrs.entity.Review;
import com.example.mrs.entity.ReviewComment;
import com.example.mrs.entity.User;
import com.example.mrs.repository.ReviewCommentRepository;
import com.example.mrs.repository.ReviewRepository;
import com.example.mrs.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MyPageController {

    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewCommentRepository reviewCommentRepository;

    //마이페이지
    @GetMapping("/mypage")
    public String myPage(HttpSession session, Model model) {

        //로그인한 유저 정보 가져오기
        UserDTO userDTO = (UserDTO) session.getAttribute("user");
        if (userDTO == null) {
            return "redirect:/login";
        }

        //리뷰 정보만 처리
        List<Review> myReviews = reviewRepository.findByUser_UserId(userDTO.getUserId());
        int reviewCount = myReviews.size();

        //모델에 담기
        model.addAttribute("user", userDTO);
        model.addAttribute("reviewCount", reviewCount);
        model.addAttribute("myReviews", myReviews);

        return "mypage";
    }

    //정보 수정
    @GetMapping("/mypage/edit")
    public String myPageEdit(HttpSession session, Model model) {
        UserDTO userDTO = (UserDTO) session.getAttribute("user");
        User user = userRepository.findByUserId(userDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("유저 없음"));
        UserUpdateDTO userUpdateDTO = UserUpdateDTO.fromEntity(user);

        model.addAttribute("user", userDTO);
        model.addAttribute("userUpdateForm", userUpdateDTO);

        return "user-edit";
    }

    @PostMapping("/mypage/edit")
    public String myPageSubmit(@ModelAttribute UserUpdateDTO userUpdateDTO, HttpSession session, Model model) {
        UserDTO userDTO = (UserDTO) session.getAttribute("user");

        User user = userRepository.findByUserId(userDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        // 이름/이메일 수정 (항상 가능)
        user.setUserName(userUpdateDTO.getUserName());
        user.setUserEmail(userUpdateDTO.getEmail());

        // 비밀번호 변경 의사 확인
        if (userUpdateDTO.getNewPassword() != null && !userUpdateDTO.getNewPassword().isEmpty()) {

            // 현재 비밀번호 입력했는지 확인
            if (userUpdateDTO.getCurrentPassword() == null || userUpdateDTO.getCurrentPassword().isEmpty()) {
                model.addAttribute("updateError", "비밀번호 변경을 위해 현재 비밀번호를 입력하세요.");
                model.addAttribute("userUpdateForm", userUpdateDTO);
                return "user-edit";
            }

            // 현재 비밀번호 일치 여부 확인
            if (!user.getUserPassword().equals(userUpdateDTO.getCurrentPassword())) {
                model.addAttribute("updateError", "현재 비밀번호가 일치하지 않습니다.");
                model.addAttribute("userUpdateForm", userUpdateDTO);
                return "user-edit";
            }

            // 새 비밀번호 확인 입력값 일치 여부 확인
            if (!userUpdateDTO.getNewPassword().equals(userUpdateDTO.getConfirmPassword())) {
                model.addAttribute("updateError", "새 비밀번호와 비밀번호 확인이 일치하지 않습니다.");
                model.addAttribute("userUpdateForm", userUpdateDTO);
                return "user-edit";
            }

            // 비밀번호 변경
            user.setUserPassword(userUpdateDTO.getNewPassword());
        }

        userRepository.save(user);

        // 세션 업데이트 (이름, 이메일만 DTO 갱신)
        userDTO.setUserName(user.getUserName());
        userDTO.setEmail(user.getUserEmail());
        session.setAttribute("user", userDTO);

        return "redirect:/mypage";
    }

    //회원 탈퇴
    @PostMapping("/user/delete")
    public String userDelete(HttpSession session, Model model) {
        UserDTO userDTO = (UserDTO) session.getAttribute("user");
        User user = userRepository.findByUserId(userDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        //User 테이블 탈퇴 표시
        user.setWithdrawal(true);
        userRepository.save(user);

        //ReviewComment 테이블에서도 해당 유저가 작성한 댓글 전부 수정
        List<ReviewComment> comments = reviewCommentRepository.findByUser_UserId(user.getUserId());
        for (ReviewComment comment : comments) {
            comment.setWithdrawal(true);
        }
        reviewCommentRepository.saveAll(comments);

        return "redirect:/";
    }

}
