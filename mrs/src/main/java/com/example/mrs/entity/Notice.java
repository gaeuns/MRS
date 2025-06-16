package com.example.mrs.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 2000)
    private String content;

    // 사용자 이름 (저장용, 외래키 아님)
    private String author;

    @Column(nullable = false)
    private String importance;

    private int likeCount = 0;
    private int viewCount;

    private LocalDateTime createdAt = LocalDateTime.now();

    // 실제 작성자 객체 (외래키 연관관계 설정)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")  // FK: notice.user_id → user.id
    private User user;

    public boolean isNew() {
        return createdAt != null && createdAt.isAfter(LocalDateTime.now().minusDays(3));
    }
}
