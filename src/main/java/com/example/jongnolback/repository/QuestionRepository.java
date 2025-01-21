package com.example.jongnolback.repository;

import com.example.jongnolback.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    // 기본적인 CRUD 메소드는 JpaRepository에서 제공됩니다.

    // 예시: 특정 퀴즈에 속한 모든 문제들을 조회하는 메소드
    List<Question> findByQuizId(Long quizId);
}