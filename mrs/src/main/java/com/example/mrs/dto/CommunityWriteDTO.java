package com.example.mrs.dto;

import com.example.mrs.entity.User;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 커뮤니티 게시글 작성/수정 시 사용되는 데이터 전송 객체 (DTO)
 * 클라이언트에서 서버로 게시글 데이터를 전송할 때 사용
 */
@Data
public class CommunityWriteDTO {

    /* 게시글 ID (수정 시 사용) */
    private Long id;

    /*게시글 제목 */
    private String title;

    /* 게시글 내용 */
    private String content;

    /* 게시글 카테고리 */
    private String category;

    /* 게시글 작성일시 */
    private LocalDateTime create_date;

    /*
     * CommunityWriteDTO 생성자
     *
     * title 게시글 제목
     * content 게시글 내용
     * category 게시글 카테고리
     */
    public CommunityWriteDTO(String title, String content, String category) {
        this.title = title;
        this.content = content;
        this.category = category;
    }

    // Getter 메서드들
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreate_date() {
        return create_date;
    }

    public void setCreate_date(LocalDateTime create_date) {
        this.create_date = create_date;
    }

    /*
     * 객체의 문자열 표현을 반환
     * 디버깅 및 로깅 목적으로 사용
     *
     * 객체 정보를 포함한 문자열
     */
    @Override
    public String toString() {
        return "CommunityWriteDTO{" +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", category='" + category + '\'' +
                ", id=" + id +
                '}';
    }
}