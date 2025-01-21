package com.example.jongnolback.entity;

import com.example.jongnolback.dto.QuestionDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "JN_QUESTION")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 문제 고유 ID

    @Column(nullable = false)
    private int quizNumber; // 퀴즈 번호 (문제의 순서)

    @Column(nullable = false)
    private int type; // 문제 유형 (0: 객관식, 1: 주관식)

    @Column(nullable = false)
    private String subtitle; // 문제 제목 (설명)

    @Column(nullable = false)
    private String tanswer; // 정답

    @ElementCollection
    @CollectionTable(name = "question_fanswers", joinColumns = @JoinColumn(name = "question_id"))
    @Column(name = "fanswer")
    private List<String> fanswers;

    private String imageBox; // 문제 이미지 (Base64 또는 파일 경로)

    // Quiz 엔티티와의 관계 설정 (단방향)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz; // Quiz와 연결 (단방향 관계)

    public QuestionDTO toDTO() {
        return QuestionDTO.builder()
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
