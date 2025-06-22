package com.example.mrs;

import com.example.mrs.entity.*;
import com.example.mrs.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@SpringBootTest
public class DataInsertTest {

    @Autowired private UserRepository userRepository;
    @Autowired private MovieRepository movieRepository;
    @Autowired private CommunityRepository communityRepository;
    @Autowired private CommunityCommentRepository communityCommentRepository;
    @Autowired private ReviewRepository reviewRepository;
    @Autowired private ReviewCommentRepository reviewCommentRepository;
    @Autowired private UserReviewRepository userReviewRepository;
    @Autowired private NoticeRepository noticeRepository;

    private final Random random = new Random();
    private final String[] categories = {"DISCUSSION", "RECOMMENDATION", "QUESTION", "SPOILER"};

    @Test
    public void insertAllData() {
        // 전체 초기화
        userReviewRepository.deleteAll();
        reviewCommentRepository.deleteAll();
        reviewRepository.deleteAll();
        communityCommentRepository.deleteAll();
        communityRepository.deleteAll();
        movieRepository.deleteAll();
        noticeRepository.deleteAll();
        userRepository.deleteAll();

        // 관리자 생성
        User admin = new User();
        admin.setUserId("admin");
        admin.setUserName("관리자");
        admin.setUserEmail("admin@example.com");
        admin.setUserPassword("admin123");
        admin.setUserRole(UserRole.ADMIN);
        admin.setWithdrawal(false);
        userRepository.save(admin);

        // 일반 사용자 생성
        for (int i = 1; i <= 3; i++) {
            User user = new User();
            user.setUserId("user" + i);
            user.setUserName("사용자" + i);
            user.setUserEmail("user" + i + "@example.com");
            user.setUserPassword("1234");
            user.setUserRole(UserRole.USER);
            user.setWithdrawal(false);
            userRepository.save(user);
        }

        List<User> normalUsers = userRepository.findAll().stream()
                .filter(u -> u.getUserRole() == UserRole.USER)
                .toList();

        // 영화 삽입 5개
        createMovie("어벤져스: 엔드게임", "액션", "안소니 루소", "2019-04-26", "/uploads/avengers.jpg", 181, "어벤져스의 마지막 전투가 시작된다.");
        createMovie("인셉션", "SF", "크리스토퍼 놀란", "2010-07-16", "/uploads/inception.jpg", 148, "꿈 속의 꿈, 복잡한 스토리의 SF 명작.");
        createMovie("라라랜드", "로맨스", "데이미언 셔젤", "2016-12-09", "/uploads/lalaland.jpg", 128, "꿈을 쫓는 두 남녀의 사랑 이야기.");
        createMovie("인터스텔라", "SF", "크리스토퍼 놀란", "2014-11-07", "/uploads/interstellar.jpg", 169, "우주를 넘나드는 시간과 공간의 대서사시.");
        createMovie("겨울왕국", "애니메이션", "크리스 벅", "2013-11-27", "/uploads/frozen.jpg", 102, "엘사의 마법과 안나의 모험, 겨울왕국의 이야기.");

        List<Movie> movies = movieRepository.findAll();

        // 커뮤니티 + 댓글 삽입
        for (User user : normalUsers) {
            for (int i = 1; i <= 5; i++) {
                Community community = new Community();
                community.setTitle(user.getUserName() + "의 글 " + i);
                community.setContent("이것은 내용 " + i);
                community.setCategory(categories[random.nextInt(categories.length)]);
                community.setUser(user);
                community.setCreateDate(LocalDateTime.now());
                community.setViewCount(random.nextInt(100));
                community.setLikeCount(random.nextInt(50));
                community.setDislikeCount(random.nextInt(10));
                community.setCommentCount(3);
                communityRepository.save(community);

                for (int j = 1; j <= 3; j++) {
                    CommunityComment comment = new CommunityComment();
                    comment.setCommunity(community);
                    // 댓글 작성자는 랜덤 유저로 지정
                    User randomUser = normalUsers.get(random.nextInt(normalUsers.size()));
                    comment.setUser(randomUser);
                    comment.setContent("댓글 " + j);
                    comment.setCreateDate(LocalDateTime.now());
                    communityCommentRepository.save(comment);
                }
            }
        }

        // 리뷰 + 리뷰댓글 + 좋아요
        for (Movie movie : movies) {
            for (User user : normalUsers) {
                Review review = new Review();
                review.setMovie(movie);
                review.setUser(user);
                review.setTitle(movie.getTitle() + " 후기 by " + user.getUserName());
                review.setDescription("정말 재미있음");
                review.setRating(5);
                review.setCreatedAt(LocalDateTime.now());
                review.setCommentCount(1);
                review.setLikeCount(random.nextInt(20));
                review.setDislikeCount(random.nextInt(5));
                review.setViewCount(random.nextInt(100));
                review.setHasSpoiler(false);
                reviewRepository.save(review);

                ReviewComment reviewComment = new ReviewComment();
                reviewComment.setReview(review);
                reviewComment.setUser(user);
                reviewComment.setContent("공감합니다!");
                reviewComment.setCreatedAt(LocalDateTime.now());
                reviewCommentRepository.save(reviewComment);

                UserReview userReview = new UserReview();
                userReview.setReview(review);
                userReview.setUser(user);
                userReviewRepository.save(userReview);
            }
        }

        // 공지사항
        for (int i = 1; i <= 3; i++) {
            Notice notice = new Notice();
            notice.setTitle("공지 " + i);
            notice.setContent("공지사항 내용 " + i);
            notice.setImportance((i == 1 ? Importance.IMPORTANT : Importance.NORMAL).name());
            notice.setAuthor(admin.getUserName());
            notice.setImagePath("/uploads/notice" + i + ".jpg");
            notice.setUser(admin);
            notice.setCreatedAt(LocalDateTime.now());
            noticeRepository.save(notice);
        }

        System.out.println("✅ 전체 더미데이터 삽입 완료!");
    }

    private void createMovie(String title, String genre, String director, String releaseDate,
                             String imagePath, int runtime, String synopsis) {
        Movie movie = new Movie();
        movie.setTitle(title);
        movie.setCategory(genre);
        movie.setDirector(director);
        movie.setReleaseDate(releaseDate);
        movie.setPosterUrl(imagePath);
        movie.setRuntime(runtime);
        movie.setSynopsis(synopsis);
        movie.setReviewSum(15);
        movie.setAverageRating(5);
        movie.setReviewCount(3);
        movie.setCountry("미국");
        movie.setActor("주연 배우");
        movie.setRating("12세 관람가");
        movie.setTrailerUrl("https://youtube.com/sample");
        movieRepository.save(movie);
    }
}
