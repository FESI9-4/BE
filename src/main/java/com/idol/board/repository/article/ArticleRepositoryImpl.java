package com.idol.board.repository.article;

import com.idol.board.domain.entity.Article;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ArticleRepositoryImpl implements ArticleRepositoryCustom {
    private final EntityManager entityManager;

    public Optional<Article> findByArticleId(Long articleId){
        String sql = "select a from Article a where a.isDeleted = false and  a.articleId = :articleId";  // isDeleted 조건 제거
        Query query = entityManager.createQuery(sql)
                .setParameter("articleId", articleId);

        try {
            return Optional.ofNullable((Article) query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

}
