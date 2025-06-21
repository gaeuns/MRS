package com.example.mrs.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 커뮤니티 게시글의 댓글을 나타내는 JPA 엔티티 클래스
 * 댓글 내용, 작성자, 작성일시, 소속 게시글과의 관계를 정의
 */
@Data
@Entity
public class CommunityComment {
    /** 댓글 고유 식별자 (자동 증가) */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 댓글 내용 */
    private String content;

    /** 댓글 작성일시 */
    @Column(name = "create_Date")
    private LocalDateTime createDate;

    /**
     * 댓글 작성자와의 다대일 관계
     * User 테이블의 userId를 외래키로 참조
     * LAZY 로딩으로 필요할때만 사용하게 설정하였음
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "userId")
    private User user;

    /**
     * 댓글이 속한 게시글과의 다대일 관계
     * Community 테이블의 id를 외래키로 참조 (기본값이므로 referencedColumnName 생략)
     * 여기도 똑같이 LAZY 로딩으로 필요할때만 사용 설정함
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "community_id")
    private Community community;

    /**
     * 엔티티가 영속화되기 전에 실행되는 콜백 메서드
     * 댓글 작성일시를 현재 시간으로 자동 설정
     */
    @PrePersist
    public void prePersist() {
        this.createDate = LocalDateTime.now();
    }
}