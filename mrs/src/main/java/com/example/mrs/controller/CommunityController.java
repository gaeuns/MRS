package com.example.mrs.controller;

import com.example.mrs.Service.CommunityCommentService;
import com.example.mrs.dto.CommunityWriteDTO;
import com.example.mrs.dto.UserDTO;
import com.example.mrs.entity.Community;
import com.example.mrs.entity.CommunityComment;
import com.example.mrs.entity.User;
import com.example.mrs.repository.CommunityRepository;
import com.example.mrs.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/*
 * 커뮤니티 게시판 관련 요청을 처리하는 컨트롤러
 * 게시글 조회, 작성, 수정, 삭제 및 댓글, 좋아요/싫어요 기능을 제공
 */
@Controller
public class CommunityController {

    // 커뮤니티 게시글 데이터 접근을 위한 리포지토리
    @Autowired
    private CommunityRepository communityrepository;

    // 사용자 데이터 접근을 위한 리포지토리
    @Autowired
    private UserRepository userRepository;

    // 댓글 관련 비즈니스 로직을 처리하는 서비스
    @Autowired
    private CommunityCommentService commentService;

    /*
     * 커뮤니티 게시판 메인 페이지를 표시
     * 카테고리별 필터링, 키워드 검색, 정렬 기능 제공
     *
     * @param category 게시글 카테고리 (기본값: "ALL")
     * @param keyword 검색 키워드 (선택사항)
     * @param sort 정렬 방식 (선택사항)
     * @param model 뷰에 데이터를 전달하기 위한 모델 객체
     * @return 커뮤니티 메인 페이지 뷰 이름
     */
    @GetMapping("/community")
    public String communityPage(@RequestParam(defaultValue = "ALL") String category,
                                @RequestParam(required = false) String keyword,
                                @RequestParam(required = false) String sort, Model model) {

        // 조건에 맞는 게시글 목록 조회
        List<Community> posts = communityrepository.CommunityList(category, keyword, sort);

        // 뷰에 전달할 데이터 설정
        model.addAttribute("posts", posts);
        model.addAttribute("category", category);
        return "community";
    }

    /*
     * 게시글 작성 페이지를 표시
     *
     * @return 게시글 작성 페이지 뷰 이름
     */
    @GetMapping("/community/write")
    public String Write() {
        return "communitywrite";
    }

    /*
     * 게시글 수정 페이지를 표시
     * 기존 게시글 데이터를 불러와서 수정 폼에 표시
     *
     * @param id 수정할 게시글 ID
     * @param dto 게시글 데이터 전송 객체
     * @param model 뷰에 데이터를 전달하기 위한 모델 객체
     * @return 게시글 작성/수정 페이지 뷰 이름
     */
    @GetMapping("/community/{id}/edit")
    public String edit(@PathVariable Long id, @ModelAttribute CommunityWriteDTO dto, Model model) {
        // 수정할 게시글 조회
        Community post = communityrepository.findById(id).orElse(null);
        model.addAttribute("post", post);

        return "communitywrite";
    }

    /*
     * 새로운 게시글을 작성하여 저장
     * 세션에서 현재 로그인한 사용자 정보를 가져와서 게시글 작성자로 설정
     *
     * @param dto 게시글 작성 데이터
     * @param userdto 세션에 저장된 사용자 정보
     * @return 커뮤니티 메인 페이지로 리다이렉트
     */
    @PostMapping("/community/write")
    public String writeCommunity(@ModelAttribute CommunityWriteDTO dto,
                                 @SessionAttribute("user") UserDTO userdto) {

        // 세션에서 사용자 ID 추출
        String userId = userdto.getUserId();

        // 사용자 정보 조회 (존재하지 않으면 예외 발생)
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalStateException("사용자가 없는디용 ㅋㅋㅋ"));

        // 새로운 게시글 엔티티 생성 및 데이터 설정
        Community community = new Community();
        community.setTitle(dto.getTitle());
        community.setContent(dto.getContent());
        community.setCategory(dto.getCategory());
        community.setCreateDate(LocalDateTime.now()); // 현재 날짜로 생성일 설정
        community.setUser(user); // 작성자 설정

        // 게시글 저장
        communityrepository.save(community);
        return "redirect:/community";
    }

    /*
     * 특정 게시글의 상세 내용을 표시
     * 조회수 증가 및 댓글 목록도 함께 조회
     *
     * id 조회할 게시글 ID
     * model 뷰에 데이터를 전달하기 위한 모델 객체
     * 리턴값 게시글 상세 페이지 뷰 이름
     */
    @GetMapping("/community/{id}")
    public String view(@PathVariable("id") Long id, Model model) {
        // 게시글 조회
        Community post = communityrepository.findById(id).orElse(null);

        // 조회수 증가
        post.setViewCount(post.getViewCount()+1);

        // 해당 게시글의 댓글 목록 조회
        List<CommunityComment> comments = commentService.commentsByCommunityRead(id);

        // 뷰에 전달할 데이터 설정
        model.addAttribute("comments", comments);
        model.addAttribute("post", post);

        // 조회수 증가된 게시글 저장
        communityrepository.save(post);
        return "communityview";
    }

    /*
     * 기존 게시글을 수정하여 저장
     *
     * id 수정할 게시글 ID
     * dto 수정된 게시글 데이터
     * userdto 세션에 저장된 사용자 정보
     * 리턴값 커뮤니티 메인 페이지로 리다이렉트
     */
    @PostMapping("/community/{id}/edit")
    public String updateCommunity(@PathVariable Long id,
                                  @ModelAttribute CommunityWriteDTO dto,
                                  @SessionAttribute("user") UserDTO userdto) {

        // 수정할 게시글 조회 (존재하지 않으면 예외 발생)
        Community post = communityrepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글 하나도 없는디?"));

        // 게시글 정보 업데이트
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        post.setCategory(dto.getCategory());

        // 수정된 게시글 저장
        communityrepository.save(post);
        return "redirect:/community";
    }

    /*
     * 게시글을 삭제
     *
     * id 삭제할 게시글 ID
     * 리턴값 커뮤니티 메인 페이지로 리다이렉트
     */
    @PostMapping("community/{id}/delete")
    public String DeleteCommunity(@PathVariable Long id) {
        // 삭제할 게시글 조회
        Community post = communityrepository.findById(id).orElse(null);

        // 게시글 삭제
        communityrepository.delete(post);

        return "redirect:/community";
    }

    /*
     * 게시글에 좋아요 추가
     * 좋아요 수를 1 증가시킴
     *
     * id 좋아요를 누를 게시글 ID
     * dto 게시글 데이터 전송 객체
     * 리턴값 해당 게시글 상세 페이지로 리다이렉트
     */
    @PostMapping("community/{id}/like")
    public String Like(@PathVariable Long id, @ModelAttribute CommunityWriteDTO dto) {
        // 게시글 조회
        Community post = communityrepository.findById(id).orElse(null);

        // 좋아요 수 증가
        post.setLikeCount(post.getLikeCount()+1);
        communityrepository.save(post);

        return "redirect:/community/{id}";
    }

    /**
     * 게시글에 싫어요 추가
     * 싫어요 수를 1 증가시킴
     *
     * id 싫어요를 누를 게시글 ID
     * dto 게시글 데이터 전송 객체
     * 해당 게시글 상세 페이지로 리다이렉트
     */
    @PostMapping("community/{id}/dislike")
    public String DisLike(@PathVariable Long id, @ModelAttribute CommunityWriteDTO dto) {
        // 게시글 조회
        Community post = communityrepository.findById(id).orElse(null);

        // 싫어요 수 증가
        post.setDislikeCount(post.getDislikeCount()+1);
        communityrepository.save(post);

        return "redirect:/community/{id}";
    }

    /**
     * 게시글에 댓글 작성
     * 댓글 수도 함께 증가시킴
     *
     * @param id 댓글을 작성할 게시글 ID
     * @param content 댓글 내용
     * @param userdto 세션에 저장된 사용자 정보
     * @return 해당 게시글 상세 페이지로 리다이렉트
     */
    @PostMapping("/community/{id}/comment")
    public String commentWrite(@PathVariable Long id,
                               @RequestParam String content,
                               @SessionAttribute("user") UserDTO userdto) {

        // 세션에서 사용자 ID 추출
        String user = userdto.getUserId();

        // 게시글 조회 및 댓글 수 증가
        Community post = communityrepository.findById(id).orElse(null);
        post.setCommentCount(post.getCommentCount()+1);
        communityrepository.save(post);

        // 댓글 작성 서비스 호출
        commentService.commentWrite(id, content, user);

        return "redirect:/community/" + id;
    }

    /**
     * 댓글 삭제
     * 해당 게시글의 댓글 수도 함께 감소시킨다
     *
     * @param id 삭제할 댓글 ID
     * @param communityId 댓글이 속한 게시글 ID
     * @return 해당 게시글 상세 페이지로 리다이렉트
     */
    @PostMapping("/community/comment/{id}/delete/{communityId}")
    public String commentDelete(@PathVariable Long id, @PathVariable Long communityId) {
        // 게시글 조회 및 댓글 수 감소
        Community post = communityrepository.findById(communityId).orElse(null);
        post.setCommentCount(post.getCommentCount()-1);
        communityrepository.save(post);

        // 댓글 삭제 서비스 호출
        commentService.deleteComment(id);
        return "redirect:/community/" + communityId;
    }

    /**
     * 댓글 수정
     *
     * @param content 수정된 댓글 내용
     * @param commentId 수정할 댓글 ID
     * @param communityId 댓글이 속한 게시글 ID
     * @return 해당 게시글 상세 페이지로 리다이렉트
     */
    @PostMapping("/community/comment/update")
    public String commentUpdate(@RequestParam String content,
                                @RequestParam Long commentId,
                                @RequestParam Long communityId) {
        // 댓글 수정 서비스 호출
        commentService.updateComment(content, commentId);
        return "redirect:/community/" + communityId;
    }
}