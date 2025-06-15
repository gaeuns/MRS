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
import java.util.ArrayList;
import java.util.Date;
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


    @PostMapping("/community/{id}/edit")
    public String edit(@PathVariable Long id,
                       @ModelAttribute CommunityWriteDTO dto,
                       @SessionAttribute("user") User user) {


        // 기존 게시글 불러오기
        Community existingPost = communityrepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다: " + id));


        // 게시글 정보 수정
        existingPost.setTitle(dto.getTitle());
        existingPost.setContent(dto.getContent());
        existingPost.setCategory(dto.getCategory());

        // 저장
        communityrepository.save(existingPost);

        return "redirect:/community";
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








}