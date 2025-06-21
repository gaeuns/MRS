package com.example.mrs.repository;

import com.example.mrs.entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
<<<<<<< HEAD

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
=======
import java.util.Optional;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

    // 중요도(중요 / 일반)로 전체 목록 조회 (예: 중요 공지 고정용)
    List<Notice> findByImportance(String importance);

    // 중요도 + 페이징 처리용
    Page<Notice> findByImportance(String importance, Pageable pageable);


>>>>>>> ef1b919951c19ce97bbfeaffacf47abcde7169ae
}
