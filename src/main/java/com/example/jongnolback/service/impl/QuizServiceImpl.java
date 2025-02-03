package com.example.jongnolback.service.impl;

import com.example.jongnolback.dto.QuestionDTO;
import com.example.jongnolback.dto.QuizDTO;
import com.example.jongnolback.entity.CustomUserDetails;
import com.example.jongnolback.entity.Question;
import com.example.jongnolback.entity.Quiz;
import com.example.jongnolback.entity.User;
import com.example.jongnolback.repository.QuestionRepository;
import com.example.jongnolback.repository.QuizRepository;
import com.example.jongnolback.repository.QuizRepositoryCustom;
import com.example.jongnolback.repository.UserRepository;
import com.example.jongnolback.service.QuizService;
import com.mysql.cj.protocol.x.Notice;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class QuizServiceImpl implements QuizService {
    private final QuizRepository quizRepository;
    private final QuestionRepository questionRepository;

    public QuizDTO createQuiz(QuizDTO quizDTO, User user) {
        // QuizDTO -> Quiz 엔티티로 변환
        Quiz quiz = Quiz.builder()
                .title(quizDTO.getTitle())
                .description(quizDTO.getDescription())
                .thumbnail(quizDTO.getThumbnail())
                .createdAt(quizDTO.getCreatedAt() != null ? LocalDateTime.parse(quizDTO.getCreatedAt()) : null)
                .user(user)
                .build();

        // 퀴즈 저장
        quiz = quizRepository.save(quiz);

        // 문제들 저장
        if (quizDTO.getQuestions() != null) {
            for (QuestionDTO questionDTO : quizDTO.getQuestions()) {
                Question question = Question.builder()
                        .quiz(quiz)
                        .subtitle(questionDTO.getSubtitle())
                        .type(questionDTO.getType())
                        .tanswer(questionDTO.getTanswer())
                        .fanswers(questionDTO.getFanswers())
                        .imageBox(questionDTO.getImageBox())
                        .build();
                // 문제 저장
                questionRepository.save(question);
                System.out.println(question.getFanswers());
            }
        }

        // 저장된 퀴즈를 DTO로 변환하여 반환
        return quiz.toDTO();
    }

    @Override
    public List<QuizDTO> getQuizzes(String searchCondition, String searchKeyword, int offset, int limit) {
        List<Quiz> quizzes = quizRepository.searchAll(searchCondition, searchKeyword, offset, limit);

        return quizzes.stream()
                .map(quiz -> QuizDTO.builder()
                        .id(quiz.getId())
                        .title(quiz.getTitle())
                        .description(quiz.getDescription())
                        .createdAt(quiz.getCreatedAt().toString())
                        .thumbnail(quiz.getThumbnail())
                        .userId(quiz.getUser().getId())
                        .questionsCount(quiz.getQuestions().size())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public long getQuizCount() {
        return quizRepository.count();
    }

    @Override
    public Quiz findById(Long id) {

        Quiz question =  quizRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Quiz not found with id: " + id));

        Quiz quiz = Quiz.builder()
                .id(question.getId())
                .title(question.getTitle())
                .description(question.getDescription())
                .createdAt(question.getCreatedAt())
                .user(question.getUser())
                .questions(question.getQuestions())
                .build();

        return quiz;
    }

    @Override
    public List<QuizDTO> getMyQuizzes(CustomUserDetails customUserDetails, int offset, int limit) {
        long userId = customUserDetails.getUser().getId();
        List<Quiz> quizzes = quizRepository.findQuizzesByUserId(userId, offset, limit);

        return quizzes.stream().map(quiz -> QuizDTO.builder()
                         .id(quiz.getId())
                        .title(quiz.getTitle())
                        .description(quiz.getDescription())
                        .createdAt(quiz.getCreatedAt().toString())
                        .thumbnail(quiz.getThumbnail())
                        .userId(quiz.getUser().getId())
                        .questionsCount(quiz.getQuestions().size())
                        .build())
                .collect(Collectors.toList());
    }
}
