package com.example.jongnolback.repository;

import com.example.jongnolback.entity.CustomUserDetails;
import com.example.jongnolback.entity.Quiz;

import java.util.List;

public interface QuizRepositoryCustom {
    List<Quiz> searchAll(String searchCondition, String searchKeyword, int offset, int limit);

    List<Quiz> findQuizzesByUserId(long userId, int offset, int limit);
}
