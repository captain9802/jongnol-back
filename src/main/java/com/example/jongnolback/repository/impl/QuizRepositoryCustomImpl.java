package com.example.jongnolback.repository.impl;

import com.example.jongnolback.entity.Quiz;
import com.example.jongnolback.repository.QuizRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class QuizRepositoryCustomImpl implements QuizRepositoryCustom {

    private final EntityManager entityManager;

    // 생성자 주입
    public QuizRepositoryCustomImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Quiz> searchAll(String searchCondition, String searchKeyword, int offset, int limit) {
        StringBuilder queryStr = new StringBuilder("SELECT q FROM Quiz q WHERE 1=1");

        if (!"all".equalsIgnoreCase(searchKeyword) && searchKeyword != null && !searchKeyword.isEmpty()) {
            queryStr.append(" AND (q.title LIKE :searchKeyword OR q.description LIKE :searchKeyword)");
        }

        TypedQuery<Quiz> query = entityManager.createQuery(queryStr.toString(), Quiz.class);

        if (!"all".equalsIgnoreCase(searchKeyword) && searchKeyword != null && !searchKeyword.isEmpty()) {
            query.setParameter("searchKeyword", "%" + searchKeyword + "%");
        }

        query.setFirstResult(offset);
        query.setMaxResults(limit);

        return query.getResultList();
    }
}
