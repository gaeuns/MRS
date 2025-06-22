package com.example.mrs;

import com.example.mrs.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DataDeleteTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private CommunityRepository communityRepository;
    @Autowired
    private CommunityCommentRepository communityCommentRepository;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private ReviewCommentRepository reviewCommentRepository;
    @Autowired
    private UserReviewRepository userReviewRepository;
    @Autowired
    private NoticeRepository noticeRepository;  // ✅ Notice 추가

    @Test
    public void deleteAllData() {
        userReviewRepository.deleteAll();
        reviewCommentRepository.deleteAll();
        reviewRepository.deleteAll();
        communityCommentRepository.deleteAll();
        communityRepository.deleteAll();
        movieRepository.deleteAll();
        noticeRepository.deleteAll();  // ✅ 공지사항 삭제 포함
        userRepository.deleteAll();

        System.out.println("✅ 전체 데이터 삭제 완료");
    }
}
