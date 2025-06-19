package com.idol.board.repository.article;

import com.idol.board.domain.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long>,ArticleRepositoryCustom {

}
