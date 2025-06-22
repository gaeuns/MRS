package com.example.mrs.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * 커뮤니티 게시글을 나타내는 JPA 엔티티 클래스
 * 게시글의 기본 정보, 통계 정보, 사용자와의 관계, 댓글과의 관계를 정의
 */
@Data
@Entity
public class Community {
    /** 게시글 고유 식별자 (자동 증가) */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 게시글 제목 (필수 입력) */
    @Column(nullable = false)
    private String title;

    /** 게시글 내용 */
    private String content;

    /** 게시글 카테고리 (필수 입력) */
    @Column(nullable = false)
    private String category;

    /** 게시글 작성일 */
    @Column(name = "create_date")
    private LocalDate createDate;

    /** 조회수 (기본값: 0) */
    private int viewCount = 0;

    /** 좋아요 수 (기본값: 0) */
    private int likeCount = 0;

    /** 싫어요 수 (기본값: 0) */
    private int dislikeCount = 0;

    /** 댓글 수 (기본값: 0) */
    private int commentCount = 0;

    /** 게시글 미리보기 내용 (데이터베이스에 저장되지 않음) */
    @Transient
    private String previewContent;

    /** 새 게시글 여부 (데이터베이스에 저장되지 않음) */
    @Transient
    private boolean isNew;

    /** 이미지 포함 여부 (데이터베이스에 저장되지 않음) */
    @Transient
    private boolean hasImages;

    /**
     * 게시글 작성자와의 다대일 관계
     * User 테이블의 userId를 외래키로 참조
     */
    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    private User user;

    /**
     * 게시글에 달린 댓글들과의 일대다 관계
     * 게시글이 삭제되면 관련 댓글들도 모두 삭제됨 (CASCADE)
     */
    @OneToMany(mappedBy = "community", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommunityComment> comments = new ArrayList<>();
}