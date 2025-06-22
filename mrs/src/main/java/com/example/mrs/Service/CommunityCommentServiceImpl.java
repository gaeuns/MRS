package com.example.mrs.Service;

import com.example.mrs.entity.Community;
import com.example.mrs.entity.CommunityComment;
import com.example.mrs.entity.User;
import com.example.mrs.repository.CommunityCommentRepository;
import com.example.mrs.repository.CommunityRepository;
import com.example.mrs.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * 커뮤니티 댓글 서비스 인터페이스의 구현체
 * 댓글 관련 비즈니스 로직을 실제로 처리하는 서비스 클래스
 */
@Service
public class CommunityCommentServiceImpl implements CommunityCommentService {

    /** 댓글 데이터 접근을 위한 리포지토리 */
    private final CommunityCommentRepository commentRepository;

    /** 게시글 데이터 접근을 위한 리포지토리 */
    private final CommunityRepository communityRepository;

    /** 사용자 데이터 접근을 위한 리포지토리 */
    private final UserRepository userRepository;

    /**
     * 생성자를 통한 의존성 주입
     *
     * commentRepository 댓글 리포지토리
     * communityRepository 커뮤니티 리포지토리
     * userRepository 사용자 리포지토리
     */
    @Autowired
    public CommunityCommentServiceImpl(CommunityCommentRepository commentRepository,
                                       CommunityRepository communityRepository,
                                       UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.communityRepository = communityRepository;
        this.userRepository = userRepository;
    }

    /**
     * 새로운 댓글을 작성하여 저장
     * 게시글과 사용자 존재 여부를 확인한 후 댓글을 생성
     *
     * communityId 댓글을 작성할 게시글 ID
     * content 댓글 내용
     * userId 댓글 작성자 ID
     * 리턴값 생성된 댓글 엔티티
     * 예외처리 IllegalArgumentException 게시글 또는 사용자가 존재하지 않을 경우
     */
    @Override
    public CommunityComment commentWrite(Long communityId, String content, String userId) {
        // 게시글 존재 여부 확인
        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        // 사용자 존재 여부 확인
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 새로운 댓글 엔티티 생성 및 데이터 설정
        CommunityComment comment = new CommunityComment();
        comment.setCommunity(community);
        comment.setUser(user);
        comment.setContent(content);
        comment.setCreateDate(LocalDateTime.now()); // 현재 시간으로 작성일 설정

        // 댓글 저장 후 반환
        return commentRepository.save(comment);
    }

    /**
     * 특정 게시글의 모든 댓글을 조회
     *
     * communityId 게시글 ID
     * 리턴값 해당 게시글의 댓글 목록
     */
    @Override
    public List<CommunityComment> commentsByCommunityRead(Long communityId) {
        return commentRepository.findByCommunityIdOrderByCreateDateDesc(communityId);
    }

    /**
     * 댓글을 삭제
     *
     * id 삭제할 댓글 ID
     */
    @Override
    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }

    /**
     * 댓글 내용을 수정
     * 댓글 존재 여부를 확인한 후 내용을 업데이트
     *
     * content 수정할 댓글 내용
     * commentId 수정할 댓글 ID
     * 리턴값 수정된 댓글 엔티티
     * 예외처리 RuntimeException 댓글이 존재하지 않을 경우
     */
    @Override
    public CommunityComment updateComment(String content, Long commentId) {
        // 댓글 존재 여부 확인
        CommunityComment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("댓글이 존재하지 않습니다."));

        // 댓글 내용 수정
        comment.setContent(content);

        // 수정된 댓글 저장 후 반환
        return commentRepository.save(comment);
    }
}