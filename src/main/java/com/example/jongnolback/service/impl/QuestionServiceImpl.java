package com.example.jongnolback.service.impl;

import com.example.jongnolback.entity.Question;
import com.example.jongnolback.repository.QuestionRepository;
import com.example.jongnolback.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {
    private final QuestionRepository questionRepository;

    @Override
    public Optional<Question> findById(Long id) {
        return questionRepository.findById(id);
    }
}
