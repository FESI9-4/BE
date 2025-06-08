package com.idol.board.repository.article;

import com.idol.board.domain.entity.Article;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;


public interface ArticleRepositoryCustom {
    Optional<Article> findByArticleId(Long articleId);
}
