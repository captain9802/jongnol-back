package com.example.jongnolback.dto;

import com.example.jongnolback.entity.Quiz;
import com.example.jongnolback.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@Setter
public class QuizDTO {
    private Long id; // 퀴즈 고유 ID
    private Long userId; // 유저 ID (퀴즈를 만든 유저)
    private String title; // 퀴즈 제목
    private String description; // 퀴즈 설명
    private String thumbnail; // 썸네일 이미지 (Base64 또는 파일 경로)
    private String createdAt; // 퀴즈 생성 날짜
    private List<QuestionDTO> questions; // 문제 리스트 (각 문제를 QuestionDTO로 변환)

    // QuizDTO를 Quiz 엔티티로 변환하는 메소드
    public Quiz toEntity(User user) {
        Quiz quizEntity = Quiz.builder()
                .id(this.id)
                .user(user) // 유저는 외부에서 전달받아 연결
                .title(this.title)
                .description(this.description)
                .thumbnail(this.thumbnail)
                .createdAt(this.createdAt != null ? LocalDateTime.parse(this.createdAt) : null)
                .build();

        // 단방향 관계이므로 setQuestions가 필요없음
        return quizEntity;
    }
};