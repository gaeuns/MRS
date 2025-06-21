package com.example.mrs.dto;

import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

@Data
public class ReviewCommentDTO {
    private Long id;
    private String content;
    private Boolean withdrawal;

    // 조회용 필드
    private String userName;
    private LocalDateTime createdAt;
    private Long authorId;
    private String authorUserId;

}
