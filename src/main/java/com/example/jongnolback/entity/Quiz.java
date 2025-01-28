package com.example.jongnolback.entity;

import com.example.jongnolback.dto.QuizDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Lob
    private String thumbnail;

    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @JsonManagedReference
    private List<Question> questions = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }
    public QuizDTO toDTO() {
        return QuizDTO.builder()
                .id(this.id)
                .userId(this.user.getId())
                .title(this.title)
                .description(this.description)
                .thumbnail(this.thumbnail)
                .createdAt(this.createdAt != null ? this.createdAt.toString() : null)
                .questions(this.questions.stream()
                        .map(Question::toDTO)
                        .collect(Collectors.toList()))
                .build();
    }

}
