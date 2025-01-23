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
    private Long id;
    private int quizNumber;
    private int type;
    private String subtitle;
    private String tanswer;
    private List<String> fanswers;
    private String imageBox;

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
