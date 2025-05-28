package com.idol.board.service.article.command;

import com.idol.board.domain.entity.Article;
import com.idol.board.repository.article.ArticleJpaRepository;
import com.idol.board.usecase.article.command.DeleteArticleUseCase;
import com.idol.global.exception.ArticleNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteArticleService implements DeleteArticleUseCase {

    private final ArticleJpaRepository articleJpaRepository;

    @Override
    @Transactional
    public Long delete(Long articleId){
        Article article = articleJpaRepository.findById(articleId)
                .orElseThrow(() -> new ArticleNotFoundException(articleId));

        article.markAsDeleted();

        return articleId;
    }
}
