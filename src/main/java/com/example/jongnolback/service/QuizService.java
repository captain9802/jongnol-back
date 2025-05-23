package com.example.jongnolback.service;

import com.example.jongnolback.dto.QuizDTO;
import com.example.jongnolback.entity.CustomUserDetails;
import com.example.jongnolback.entity.Question;
import com.example.jongnolback.entity.Quiz;
import com.example.jongnolback.entity.User;

import java.util.List;

public interface QuizService {
    QuizDTO createQuiz(QuizDTO quizDTO, User user);

    List<QuizDTO> getQuizzes(String searchCondition, String searchKeyword, int offset, int limit);

    long getQuizCount();

    Quiz findById(Long id);

    List<QuizDTO> getMyQuizzes(CustomUserDetails customUserDetails, int offset, int limit);

    void deleteById(Long id);

    QuizDTO getQuizQuestions(Long id, QuizDTO quizDTO);
}
