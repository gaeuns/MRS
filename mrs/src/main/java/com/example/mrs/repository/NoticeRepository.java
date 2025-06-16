package com.example.mrs.repository;

import com.example.mrs.entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

    // 중요도(중요 / 일반)로 전체 목록 조회 (예: 중요 공지 고정용)
    List<Notice> findByImportance(String importance);

    // 중요도 + 페이징 처리용
    Page<Notice> findByImportance(String importance, Pageable pageable);


}
