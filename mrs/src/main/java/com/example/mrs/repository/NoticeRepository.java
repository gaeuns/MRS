package com.example.mrs.repository;

import com.example.mrs.entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Notice 엔티티용 JPA Repository
 */
public interface NoticeRepository extends JpaRepository<Notice, Long> {

    // 중요도(IMPORTANT, NORMAL) 전체 조회
    List<Notice> findByImportance(String importance);

    // 중요도별 페이징 조회
    Page<Notice> findByImportance(String importance, Pageable pageable);

    // 제목 키워드 검색 (대소문자 무시)
    @Query("SELECT n FROM Notice n WHERE LOWER(n.title) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Notice> findByKeyword(@Param("keyword") String keyword, Pageable pageable);

    // 중요도 + 제목 키워드 검색
    @Query("SELECT n FROM Notice n WHERE n.importance = :importance AND LOWER(n.title) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Notice> findByImportanceAndKeyword(@Param("importance") String importance,
                                            @Param("keyword") String keyword,
                                            Pageable pageable);
}
