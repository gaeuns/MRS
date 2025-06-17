package com.example.mrs.Service;

import com.example.mrs.entity.CommunityComment;

import java.util.List;

public interface CommunityCommentService {
    CommunityComment commentWrite(Long communityId, String content, String userId);
    List<CommunityComment> commentsByCommunityRead(Long communityId);
    void deleteComment(Long id);
    CommunityComment updateComment(String content, Long commentId);
}