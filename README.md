# MRS (Movie Review System)

영화 정보 등록, 리뷰/평점 작성, 커뮤니티 게시판, 공지사항 기능을 갖춘 Spring Boot 기반 팀 프로젝트입니다.

## 기술 스택

- **Backend**: Java 17, Spring Boot 3.5.3 (Spring Web, Spring Data JPA, Validation)
- **View**: Thymeleaf
- **Database**: MySQL (JPA/Hibernate, `ddl-auto=update`)
- **기타**: Lombok, Spring DevTools
- **빌드 도구**: Maven

## 주요 기능

커밋 히스토리와 컨트롤러 코드를 기준으로 정리한 기능 목록입니다.

### 회원 (Auth)
- 회원가입 / 로그인 / 로그아웃 (`/signup`, `/login`, `/logout`)
- 마이페이지 조회 및 정보 수정 (`/mypage`, `/mypage/edit`)
- 회원 탈퇴 (`/user/delete`)

### 메인 페이지
- 메인 화면 (`/`)

### 영화 & 리뷰
- 영화 등록 / 목록 / 상세보기 (`/movies`, `/movie-register`, `/movie-page/{id}`)
- 영화 삭제 (`/movie/delete/{id}`)
- 리뷰 작성 / 조회 / 삭제 (`/movies/{movieId}/review`, `/review-page/{id}`)
- 리뷰 추천 / 비추천 (`/review/{id}/like`, `/review/{id}/dislike`)
- 리뷰 댓글 작성 / 수정 / 삭제 (`/reviews/{id}/comments`)

### 커뮤니티
- 게시글 작성 / 조회 / 수정 / 삭제 (`/community`, `/community/write`, `/community/{id}`)
- 추천 / 비추천 (`/community/{id}/like`, `/community/{id}/dislike`)
- 댓글 작성 / 수정 / 삭제 (`/community/{id}/comment`)

### 공지사항
- 공지 작성 / 조회 / 수정 / 삭제 (`/notice`, `/notice/write`, `/notice/{id}`, `/notice/edit/{id}`)
- 이미지 첨부 지원

### 관리자
- 관리자 메인 페이지 (`/admin`)

## 브랜치 구조

| 브랜치 | 설명 |
|---|---|
| `main` | 최종 배포용 브랜치 (라이선스, gitignore만 반영된 초기 상태) |
| `develop` | 각 기능 브랜치를 병합해 통합한 개발 브랜치 |
| `feature/Mainpage` | 메인페이지, 회원가입/로그인/로그아웃 |
| `feature/MovieReview` | 영화 등록/조회, 리뷰 작성/수정, 마이페이지, 회원 탈퇴 |
| `feature/Community` | 커뮤니티 게시판, 댓글, 추천/비추천, (가장 최신 상태 포함) |
| `feature/Notice` | 공지사항 CRUD, 이미지 첨부, 관리자 페이지 |

## 작업자별 담당 내역

- **최가은** : 메인페이지, 회원가입/로그인/로그아웃, 영화 등록/목록/상세, 리뷰 작성/수정, 마이페이지, 회원 탈퇴, DTO 구조 리팩터링
- **임지섭** : 커뮤니티 게시판(엔티티/DTO/Controller/Repository), 댓글 기능, 추천/비추천, 게시물 삭제, Maven 프로젝트 전환, 테스트용 데이터 삽입 스크립트
- **임성용** : 공지사항 작성/수정/삭제, 이미지 첨부, 관리자 페이지

## 실행 방법

1. MySQL에 `mrs` 데이터베이스를 생성합니다.
   ```sql
   CREATE DATABASE mrs;
   ```
2. `src/main/resources/application.properties`에서 DB 계정 정보를 본인 환경에 맞게 수정합니다.
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/mrs
   spring.datasource.username=root
   spring.datasource.password=root
   ```
3. 프로젝트를 빌드 및 실행합니다.
   ```bash
   ./mvnw spring-boot:run
   ```
4. 브라우저에서 `http://localhost:8080` 접속

## 프로젝트 구조

```
mrs/
 ├─ src/main/java/com/example/mrs/
 │   ├─ controller/   # 화면/API 요청 처리
 │   ├─ service/       # 비즈니스 로직
 │   ├─ repository/    # JPA Repository
 │   ├─ entity/        # DB 엔티티
 │   ├─ dto/           # 요청/응답 DTO
 │   └─ config/        # Web/Multipart 설정
 ├─ src/main/resources/
 │   ├─ templates/     # Thymeleaf 화면
 │   └─ static/        # CSS 등 정적 자원
 └─ src/test/java/     # 테스트 코드
```
