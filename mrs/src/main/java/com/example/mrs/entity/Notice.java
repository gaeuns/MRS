package com.example.mrs.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity  // JPA 엔티티임을 선언 (DB 테이블과 매핑됨)
@Getter
@Setter  // Lombok - 자동으로 getter, setter 생성
public class Notice {

    @Id  // 기본키(primary key) 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // MySQL의 auto_increment 방식 사용하여 ID 자동 생성
    private Long id;

    @Column  // 공지사항 제목
    private String title;

    @Column(length = 2000)  // 공지 내용 (최대 2000자)
    private String content;

    // 작성자 이름 (문자열만 저장, User 엔티티와 별개로 단순 이름 저장)
    private String author;

    @Column(nullable = false)  // 중요도 필수값 (IMPORTANT, NORMAL 등)
    private String importance;

    // 첨부 이미지 경로 (저장된 파일명 경로를 문자열로 저장)
    private String imagePath;

    // 조회수 (기본 0, 상세페이지 접근시 증가)
    private int viewCount;

    // 생성일 (공지 작성 시간 기록, 객체 생성 시 자동 기록)
    private LocalDateTime createdAt = LocalDateTime.now();

    // 작성자 사용자 객체와 연관관계 설정 (다대일 관계: 한 명의 사용자가 여러 개의 공지 작성 가능)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")  // 외래키 이름 지정 (notice 테이블의 user_id 컬럼)
    private User user;

    // 최근 3일 이내 작성된 새 글인지 여부를 판별하는 유틸리티 메서드
    public boolean isNew() {
        return createdAt != null && createdAt.isAfter(LocalDateTime.now().minusDays(3));
    }
}
