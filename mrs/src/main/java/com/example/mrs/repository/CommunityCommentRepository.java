package com.example.mrs.repository;

import com.example.mrs.entity.CommunityComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 커뮤니티 댓글 데이터 접근을 위한 JPA 리포지토리 인터페이스
 * 댓글의 CRUD 작업 및 커스텀 쿼리 메서드를 제공
 */
public interface CommunityCommentRepository extends JpaRepository<CommunityComment, Long> {

    /**
     * 특정 게시글에 달린 모든 댓글을 조회
     *
     *communityId 게시글 ID
     * OrderBy CreateDateDesc 날짜기준으로 내림차순(최신댓글 -> 오래된댓글) 순서로 정렬
     *해당 게시글의 댓글 목록
     */
    List<CommunityComment> findByCommunityIdOrderByCreateDateDesc(Long communityId);
}