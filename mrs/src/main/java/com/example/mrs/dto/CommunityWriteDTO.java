package com.example.mrs.dto;

import com.example.mrs.entity.User;
import lombok.Data;


@Data
public class CommunityWriteDTO {

    private Long id;
    private String title;
    private String content;
    private String category;


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
