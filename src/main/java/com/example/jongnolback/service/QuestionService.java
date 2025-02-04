package com.example.jongnolback.service;

import com.example.jongnolback.entity.Question;

import java.util.Optional;

public interface QuestionService {
    Optional<Question> findById(Long id);
}
