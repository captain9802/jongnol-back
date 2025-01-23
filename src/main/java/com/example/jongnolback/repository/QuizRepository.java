package com.example.jongnolback.repository;

import com.example.jongnolback.dto.QuizDTO;
import com.example.jongnolback.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuizRepository extends JpaRepository<Quiz, Long>, QuizRepositoryCustom {
}
