package com.example.mrs.repository;

import com.example.mrs.entity.Community;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 커뮤니티 게시글 데이터 접근을 위한 JPA 리포지토리 인터페이스
 * 게시글의 CRUD 작업 및 복잡한 검색/정렬 기능을 제공
 */
public interface CommunityRepository extends JpaRepository<Community, Long> {

    /**
     * 카테고리, 키워드, 정렬 조건에 따라 게시글 목록을 조회하는 커스텀 쿼리
     *
     * 정렬기준
     * category 게시글 카테고리 ("ALL" 또는 특정 카테고리명)
     * DB에는 ALL이라는 category가 들어가지 않음으로 ALL일경우
     * 논리연산자 or의 특징인 앞의 연산결과가 true면 자동 실행으로 b.category = :category가 실행되지 않는다.
     * 따라서 전체 게시글 조회 또는 category별 조회가 가능하게 된다.
     * 또한 최신 게시물이 맨위에 올라와야함으로 createDate DESC를 사용하여 최신글이 항상 위에 올라와있게 설정하였다.
     *
     * keyword 검색할 키워드 (제목, 내용에서 검색)
     * LIKE를 사용하여 포함된 키워드만 검색되게 설정
     *
     * sort 정렬 방식 (latest/popular/comments/views)
     * sort의 값이 들어왔을경우 case문의 :sort = 실행
     */
    @Query("SELECT b FROM Community b WHERE (:category = 'ALL' OR b.category = :category) " +
            "AND (:keyword IS NULL OR b.title LIKE %:keyword% OR b.content LIKE %:keyword%) " +
            "ORDER BY CASE WHEN :sort = 'latest' THEN b.createDate END DESC, " +
            "CASE WHEN :sort = 'popular' THEN b.likeCount END DESC, " +
            "CASE WHEN :sort = 'comments' THEN b.commentCount END DESC, " +
            "CASE WHEN :sort = 'views' THEN b.viewCount END DESC, " +
            "CASE WHEN (:category = 'ALL' OR b.category = :category) THEN b.createDate END DESC")
    List<Community> CommunityList(String category, String keyword, String sort);
}