package com.example.jongnolback.service.impl;

import com.example.jongnolback.common.FileUtils;
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
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class QuizServiceImpl implements QuizService {
    private final QuizRepository quizRepository;
    private final QuestionRepository questionRepository;
    private  final FileUtils fileUtils;
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
                // 이미지가 base64 형식으로 들어오는 경우, S3에 업로드
                String uploadedImagePath = null;
                if (questionDTO.getImageBox() != null && !questionDTO.getImageBox().isEmpty()) {
                    try {
                        MultipartFile imageFile = fileUtils.convertBase64ToMultipartFile(questionDTO.getImageBox(), "question_image.png");
                        uploadedImagePath = fileUtils.uploadFile(imageFile, "question-images/");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                Question question = Question.builder()
                        .quiz(quiz)
                        .subtitle(questionDTO.getSubtitle())
                        .type(questionDTO.getType())
                        .tanswer(questionDTO.getTanswer())
                        .fanswers(questionDTO.getFanswers())
                        .imageBox(uploadedImagePath)
                        .build();

                questionRepository.save(question);
                System.out.println(question.getFanswers());
            }
        }

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

        for (Question q : question.getQuestions()) {
            if (q.getImageBox() != null) {
                String imageBoxUrl = fileUtils.getObjectUrl(q.getImageBox());
                q.setImageBox(imageBoxUrl);  // S3 URL로 설정
            }
        }

        Quiz quiz = Quiz.builder()
                .id(question.getId())
                .title(question.getTitle())
                .description(question.getDescription())
                .thumbnail(fileUtils.getObjectUrl(question.getThumbnail()))
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

    @Override
    public void deleteById(Long id) {
        quizRepository.deleteById(id);
    }
}
