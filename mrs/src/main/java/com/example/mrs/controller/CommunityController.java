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
import java.util.List;

@Controller
public class CommunityController {

    @Autowired
    private CommunityRepository communityrepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommunityCommentService commentService;


    @GetMapping("/community")
    public String communityPage(@RequestParam(required = false) String category, Model model) {
        List<Community> posts;

        if ("DISCUSSION".equals(category)) {
            posts = communityrepository.findByCategory("DISCUSSION");
        }
        else if ("QUESTION".equals(category)) {
            posts = communityrepository.findByCategory("QUESTION");
        }
        else if ("RECOMMENDATION".equals(category)) {
            posts = communityrepository.findByCategory("RECOMMENDATION");
        }
        else if("SPOILER".equals(category))
        {
            posts = communityrepository.findByCategory("SPOILER");
        }
        else {
            posts = communityrepository.findAll();
        }

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
        
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalStateException("사용자가 없는디용 ㅋㅋㅋ"));


        Community community = new Community();
        community.setTitle(dto.getTitle());
        community.setContent(dto.getContent());
        community.setCategory(dto.getCategory());
        community.setCreate_Date(LocalDate.now());
        community.setUser(user);

        communityrepository.save(community);
        return "redirect:/community";
    }


    @GetMapping("/community/{id}")
    public String view(@PathVariable("id") Long id, Model model) {

        Community post = communityrepository.findById(id).orElse(null);
        post.setViewCount(post.getViewCount()+1);
        List<CommunityComment> comments = commentService.commentsByCommunityRead(id);

        model.addAttribute("comments", comments);
        model.addAttribute("post", post);

        communityrepository.save(post);
        return "communityview";
    }


    @PostMapping("/community/{id}/edit")
    public String updateCommunity(@PathVariable Long id,
                                  @ModelAttribute CommunityWriteDTO dto,
                                  @SessionAttribute("user") UserDTO userdto) {

        Community post = communityrepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글 하나도 없는디?"));

        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        post.setCategory(dto.getCategory());

        communityrepository.save(post);
        return "redirect:/community";
    }


    @PostMapping("community/{id}/delete")
    public String DeleteCommunity(@PathVariable Long id)
    {
        Community post = communityrepository.findById(id).orElse(null);

        communityrepository.delete(post);

        return "redirect:/community";
    }

    @PostMapping("community/{id}/like")
    public String Like(@PathVariable Long id, @ModelAttribute CommunityWriteDTO dto)
    {
        Community post = communityrepository.findById(id).orElse(null);

        post.setLikeCount(post.getLikeCount()+1);
        communityrepository.save(post);

        return "redirect:/community/{id}";
    }

    @PostMapping("community/{id}/dislike")
    public String DisLike(@PathVariable Long id, @ModelAttribute CommunityWriteDTO dto)
    {
        Community post = communityrepository.findById(id).orElse(null);
        post.setDislikeCount(post.getDislikeCount()+1);
        communityrepository.save(post);

        return "redirect:/community/{id}";
    }

    @PostMapping("/community/{id}/comment")
    public String commentWrite(@PathVariable Long id,
                               @RequestParam String content,
                               @SessionAttribute("user") UserDTO userdto) {

        String user = userdto.getUserId();
        Community post = communityrepository.findById(id).orElse(null);
        post.setCommentCount(post.getCommentCount()+1);
        communityrepository.save(post);

        commentService.commentWrite(id, content, user);

        return "redirect:/community/" + id;
    }

    @PostMapping("/community/comment/{id}/delete/{communityId}")
    public String commentDelete(@PathVariable Long id, @PathVariable Long communityId)
    {
        Community post = communityrepository.findById(communityId).orElse(null);
        post.setCommentCount(post.getCommentCount()-1);
        communityrepository.save(post);

        commentService.deleteComment(id);
        return "redirect:/community/" + communityId;
    }

    @PostMapping("/community/comment/update")
    public String commentUpdate(@RequestParam String content, @RequestParam Long commentId, @RequestParam Long communityId)
    {
        commentService.updateComment(content, commentId);
        return "redirect:/community/" + communityId;
    }

}