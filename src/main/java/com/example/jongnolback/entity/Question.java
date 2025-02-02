package com.example.jongnolback.entity;

import com.example.jongnolback.dto.QuestionDTO;
import com.fasterxml.jackson.annotation.JsonBackReference;
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
    private Long id;

    @Column(nullable = false)
    private int quizNumber;

    @Column(nullable = false)
    private int type;

    @Column(nullable = false)
    private String subtitle;

    @Column(nullable = false)
    private String tanswer;

    @ElementCollection
    @CollectionTable(name = "question_fanswers", joinColumns = @JoinColumn(name = "question_id"))
    @Column(name = "fanswer")
    private List<String> fanswers;

    @Lob
    private String imageBox;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id", nullable = false)
    @JsonBackReference
    private Quiz quiz;

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
