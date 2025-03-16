package com.example.jongnolback.dto;

import com.example.jongnolback.entity.Quiz;
import com.example.jongnolback.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@Setter
public class QuizDTO {
    private Long id;
    private Long userId;
    private String title;
    private String description;
    private String thumbnail;
    private String createdAt;
    private List<QuestionDTO> questions;
    private String searchKeyword;
    private String searchCondition;
    private int questionsCount;
    private int quizMode;

    public Quiz toEntity(User user) {
        Quiz quizEntity = Quiz.builder()
                .id(this.id)
                .user(user)
                .title(this.title)
                .description(this.description)
                .thumbnail(this.thumbnail)
                .createdAt(this.createdAt != null ? LocalDateTime.parse(this.createdAt) : null)
                .build();

        return quizEntity;
    }
};