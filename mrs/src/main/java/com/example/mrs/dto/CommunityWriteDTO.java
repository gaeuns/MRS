package com.example.mrs.dto;

import com.example.mrs.entity.User;
import lombok.Data;

import java.time.LocalDateTime;


@Data
public class CommunityWriteDTO {

    private Long id;
    private String title;
    private String content;
    private String category;
    private LocalDateTime create_date;

    public CommunityWriteDTO(String title, String content, String category)
    {
        this.title = title;
        this.content = content;
        this.category = category;
    }

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
