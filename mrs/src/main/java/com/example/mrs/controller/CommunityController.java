package com.example.mrs.controller;


import com.example.mrs.dto.CommunityWriteDTO;
import com.example.mrs.dto.UserDTO;
import com.example.mrs.entity.Community;
import com.example.mrs.entity.User;
import com.example.mrs.repository.CommunityRepository;
import com.example.mrs.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
public class CommunityController {

    @Autowired
    private CommunityRepository communityrepository;
    @Autowired
    private UserRepository userRepository;



    @GetMapping("/community")
    public String Community(@RequestParam(required = false) String category, Model model) {

        List<Community> posts = communityrepository.findAll();

        model.addAttribute("posts", posts);
        model.addAttribute("category", category);
        return "community";
    }


    @GetMapping("/community/write")
    public String Write() {

        return "communitywrite";
    }


    @GetMapping("/community/{id}/edit")
    public String edit(@PathVariable Long id, @ModelAttribute CommunityWriteDTO dto, Model model) {

        Community post = communityrepository.findById(id).orElse(null);
        model.addAttribute("post", post);

        return "communitywrite";
    }



    @PostMapping("/community/write")
    public String writeCommunity(@ModelAttribute CommunityWriteDTO dto,
                                 @SessionAttribute("user") UserDTO userdto) {


        String userId = userdto.getUserId();
        System.out.println("현재 로그인한 유저 ID: " + userId); //console에 로그인 유저 확인
        
        
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalStateException("유효하지 않은 사용자"));//익명함수 사용


        Community community = new Community();
        community.setTitle(dto.getTitle());
        community.setContent(dto.getContent());
        community.setCategory(dto.getCategory());
        community.setCreate_Date(LocalDate.now());
        community.setUser(user); // 반드시 영속 상태

        communityrepository.save(community);
        return "redirect:/community";
    }


    @GetMapping("/community/{id}")
    public String view(@PathVariable("id") Long id, Model model) {
        Community post = communityrepository.findById(id).orElse(null);
        model.addAttribute("post", post);
        return "communityview";
    }


    @PostMapping("/community/{id}/edit")
    public String updateCommunity(@PathVariable Long id,
                                  @ModelAttribute CommunityWriteDTO dto,
                                  @SessionAttribute("user") UserDTO userdto) {

        Community post = communityrepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글 없음"));

        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        post.setCategory(dto.getCategory());
        // post.setCreateDate(LocalDateTime.now()); ← 수정 시에는 생략 가능

        communityrepository.save(post); // 기존 post 수정 저장
        return "redirect:/community";
    }





}