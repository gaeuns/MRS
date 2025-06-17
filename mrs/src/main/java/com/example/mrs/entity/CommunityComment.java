package com.example.mrs.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class CommunityComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    private LocalDateTime create_Date;

    //User 엔티티에서 해당댓글에 작성한 유저의 아이디
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "userId")
    private User user;

    //댓글이 달린 게시글의 id referencedColumnName이 필요없는 이유가 기본적으로 없으면 id참고함
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "community_id")
    private Community community;

    @PrePersist
    public void prePersist() {
        this.create_Date = LocalDateTime.now();
    }

}
