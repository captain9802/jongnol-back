# 🧐 JongNol - Backend

JongNol은 퀴즈 기반 컨테츠를 생성, 풀이, 공유할 수 있는 플랫폼입니다.
이 저장소는 Spring Boot 기반으로 작성된 JongNol의 **백엔드 API 서버**입니다.

---

## 🚀 주요 기능

* 📦 사용자 인증 및 JWT 기반 보안 처리
* 🤩 퀴즈 생성, 수정, 삭제, 조회 기능
* 📝 퀴즈에 포함된 문항(Question) 관리
* 🖼️ 이미지 업로드 및 처리 기능 (Multipart + S3 또는 FileSystem)
* 👤 마이페이지 및 사용자 정보 처리

---

## 📁 프로젝트 구조 ( 일반 )

```
src/main/java/com/example/jongnolback/
├── controller/
│   ├── QuizController.java       # 퀴즈 및 문항 API
│   └── UserController.java       # 사용자 로그인 및 회원 관리 API
├── service/                      # 비즈니스 로직 처리
├── repository/                  # JPA 기반 데이터 접근
├── entity/                      # User, Quiz, Question 등 도메인 목록
├── jwt/                         # JWT 토큰 발급 및 필터 처리
└── dto/                         # API 요청 및 응답 객체
```

---

## 🛠 기술 스택

| 범위         | 기술                           |
| ---------- | ---------------------------- |
| Backend    | Spring Boot, Spring Security |
| Build Tool | Gradle                       |
| Database   | MySQL, Spring Data JPA       |
| 인증         | JWT                          |
| 파일 처리      | Multipart, FileUtils         |
| 배포/운영      | AWS , Nginx              |

---

## ⚙️ 실행 방법

### 1. 프로젝트 클론

```bash
git clone https://github.com/captain9802/jongnol-back.git
cd jongnol-back
```

### 2. 환경 변수 설정

`application.yml` 또는 `application.properties`에 아래와 같은 설정이 필요합니다:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/jongnol
    username: root
    password: your_password
  jpa:
    hibernate:
      ddl-auto: update
  servlet:
    multipart:
      enabled: true
```

### 3. 실행

```bash
./gradlew build
java -jar build/libs/jongnol-back-0.0.1-SNAPSHOT.jar
```

---

## 🧪 API 예시

### POST `/quiz/create`

* 퀴즈와 문항 등록
* 이미지 파일 업로드 지원 (multipart/form-data)

### GET `/quiz/{id}`

* 특정 퀴즈 상세 조회

### POST `/user/login`

* JWT 로그인 처리

---

## ✨ 허후 계획

* [ ] S3 연동 파일 업로드/다운로드 구현
* [ ] 퀴즈 통계 기능
* [ ] 소셜 로그인 연동 (카카오 등)

---

## 👨‍💼 개발자

* 손우성 ([@captain9802](https://github.com/captain9802))

---
