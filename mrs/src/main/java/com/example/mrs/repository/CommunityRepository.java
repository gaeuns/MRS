package com.example.mrs.repository;


import com.example.mrs.entity.Community;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommunityRepository extends JpaRepository<Community, Long> {

    @Query("SELECT b FROM Community b WHERE (:category = 'ALL' OR b.category = :category) " +
            "AND (:keyword IS NULL OR b.title LIKE %:keyword% OR b.content LIKE %:keyword%) " +
            "ORDER BY CASE WHEN :sort = 'latest' THEN b.createDate END DESC, " +
            "CASE WHEN :sort = 'popular' THEN b.likeCount END DESC, " +
            "CASE WHEN :sort = 'comments' THEN b.commentCount END DESC, " +
            "CASE WHEN :sort = 'views' THEN b.viewCount END DESC")
    List<Community> CommunityList(String category, String keyword, String sort);
}

