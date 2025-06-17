package com.example.mrs.Service;

import com.example.mrs.entity.Community;
import com.example.mrs.entity.CommunityComment;
import com.example.mrs.entity.User;
import com.example.mrs.repository.CommunityCommentRepository;
import com.example.mrs.repository.CommunityRepository;
import com.example.mrs.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommunityCommentServiceImpl implements CommunityCommentService {

    private final CommunityCommentRepository commentRepository;
    private final CommunityRepository communityRepository;
    private final UserRepository userRepository;

    @Autowired
    public CommunityCommentServiceImpl(CommunityCommentRepository commentRepository,
                                       CommunityRepository communityRepository,
                                       UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.communityRepository = communityRepository;
        this.userRepository = userRepository;
    }

    @Override
    public CommunityComment commentWrite(Long communityId, String content, String userId) {
        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        CommunityComment comment = new CommunityComment();
        comment.setCommunity(community);
        comment.setUser(user);
        comment.setContent(content);
        comment.setCreate_Date(LocalDateTime.now());

        return commentRepository.save(comment);
    }

    @Override
    public List<CommunityComment> commentsByCommunityRead(Long communityId) {
        return commentRepository.findByCommunityId(communityId);
    }

    @Override
    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }

    @Override
    public CommunityComment updateComment(String content, Long commentId)
    {
        CommunityComment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("댓글이 존재하지 않습니다."));

        comment.setContent(content);

        return commentRepository.save(comment);
    }
}
