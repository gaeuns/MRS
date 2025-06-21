package com.example.mrs.Service;

import com.example.mrs.entity.CommunityComment;

import java.util.List;

/**
 * 커뮤니티 댓글 관련 비즈니스 로직을 처리하는 서비스 인터페이스
 * 댓글의 생성, 조회, 수정, 삭제 기능을 정의
 */
public interface CommunityCommentService {

    /**
     * 새로운 댓글을 작성
     *
     * communityId 댓글을 작성할 게시글 ID
     * content 댓글 내용
     * userId 댓글 작성자 ID
     * 리턴값 생성된 댓글 엔티티
     */
    CommunityComment commentWrite(Long communityId, String content, String userId);

    /**
     * 특정 게시글의 모든 댓글을 조회
     *
     * communityId 게시글 ID
     * 리턴값 해당 게시글의 댓글 목록
     */
    List<CommunityComment> commentsByCommunityRead(Long communityId);

    /**
     * 댓글을 삭제
     *
     * id 삭제할 댓글 ID
     */
    void deleteComment(Long id);

    /**
     * 댓글 내용을 수정
     *
     * content 수정할 댓글 내용
     * commentId 수정할 댓글 ID
     * 리턴값 수정된 댓글 엔티티
     */
    CommunityComment updateComment(String content, Long commentId);
}