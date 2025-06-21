package com.example.mrs.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
public class Community {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false) //null값 미포함
    private String title;
    private String content;
    @Column(nullable = false)
    private String category;
    @Column(name = "create_date")
    private LocalDate createDate; //날짜만 필요함으로 LocalDate사용
    private int viewCount = 0;
    private int likeCount = 0;
    private int dislikeCount = 0;
    private int commentCount = 0;

    @Transient
    private String previewContent;
    @Transient
    private boolean isNew;
    @Transient
    private boolean hasImages;
    
    //user_id외래키로 user테이블의 user_id값 가져오기
    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "userId")  // 여기서 userId는 User 엔티티의 컬럼명
    private User user;

    @OneToMany(mappedBy = "community", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommunityComment> comments = new ArrayList<>();



}
