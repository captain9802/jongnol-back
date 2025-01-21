package com.example.jongnolback.entity;

import com.example.jongnolback.dto.QuizDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "JN_QUIZ")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 퀴즈 고유 ID

    @ManyToOne(fetch = FetchType.LAZY) // User와 다대일 관계
    @JoinColumn(name = "user_id", nullable = false) // 외래 키로 user_id 사용
    private User user; // 유저와 연결 (퀴즈를 만든 유저)

    @Column(nullable = false)
    private String title; // 퀴즈 제목

    @Column(nullable = false)
    private String description; // 퀴즈 설명

    @Lob
    private String thumbnail; // 퀴즈 썸네일 이미지 (Base64로 저장 가능)

    private LocalDateTime createdAt; // 퀴즈 생성 날짜

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Question> questions = new ArrayList<>(); // 퀴즈에 포함된 문제들

    @PrePersist
    public void prePersist() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now(); // 퀴즈가 생성될 때 날짜 자동 설정
        }
    }
    public QuizDTO toDTO() {
        return QuizDTO.builder()
                .id(this.id)
                .userId(this.user.getId()) // 유저의 id를 가져옴
                .title(this.title)
                .description(this.description)
                .thumbnail(this.thumbnail)
                .createdAt(this.createdAt != null ? this.createdAt.toString() : null) // createdAt을 문자열로 변환
                .questions(this.questions.stream()
                        .map(Question::toDTO) // 각 Question을 DTO로 변환
                        .collect(Collectors.toList())) // List<QuestionDTO> 반환
                .build();
    }

}
