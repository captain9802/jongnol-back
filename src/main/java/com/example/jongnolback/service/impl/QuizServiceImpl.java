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
                String uploadedImagePath = null;
                if (questionDTO.getImageBox() != null && !questionDTO.getImageBox().isEmpty()) {
                    String imageUrl = questionDTO.getImageBox();

                    if (fileUtils.isValidImageUrl(imageUrl)) {
                        String tempImagePath = imageUrl;
                        String newImagePath = "question-images/" + tempImagePath.substring(tempImagePath.lastIndexOf("/") + 1); // 새로운 경로
                        try {
                            fileUtils.copyFile(tempImagePath, newImagePath);
                            fileUtils.deleteFile(tempImagePath);
                            uploadedImagePath = fileUtils.getObjectUrl(newImagePath);
                        } catch (Exception e) {
                            uploadedImagePath = "https://jongnol-0224.s3.ap-northeast-2.amazonaws.com/default_image.png";
                        }
                    }
                }

                // 나머지 로직 처리
                Question question = Question.builder()
                        .quiz(quiz)
                        .subtitle(questionDTO.getSubtitle())
                        .type(questionDTO.getType())
                        .tanswer(questionDTO.getTanswer())
                        .fanswers(questionDTO.getFanswers())
                        .imageBox(uploadedImagePath)
                        .build();

                questionRepository.save(question);
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
                q.setImageBox(imageBoxUrl);
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
