package com.example.jongnolback.dto;

import com.example.jongnolback.entity.Question;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Builder
@Setter
public class QuestionDTO {
    private Long id; // 질문 고유 ID
    private int quizNumber; // 퀴즈 번호 (문제의 순서)
    private int type; // 문제 유형 (0: 객관식, 1: 주관식)
    private String subtitle; // 문제 제목 (설명)
    private String tanswer; // 정답
    private List<String> fanswers; // 틀린 답변들
    private String imageBox; // 문제 이미지 (Base64 또는 파일 경로)

    // QuestionDTO를 Question 엔티티로 변환하는 메소드
    public Question toEntity() {
        return Question.builder()
                .id(this.id)
                .quizNumber(this.quizNumber)
                .type(this.type)
                .subtitle(this.subtitle)
                .tanswer(this.tanswer)
                .fanswers(this.fanswers)
                .imageBox(this.imageBox)
                .build();
    }
}
