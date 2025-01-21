package com.example.jongnolback.service;

import com.example.jongnolback.dto.QuizDTO;
import com.example.jongnolback.entity.User;

public interface QuizService {
    QuizDTO createQuiz(QuizDTO quizDTO, User user);
}
