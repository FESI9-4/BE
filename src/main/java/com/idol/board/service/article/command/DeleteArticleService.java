package com.idol.board.service.article.command;

import com.idol.board.domain.entity.Article;
import com.idol.board.repository.article.ArticleRepository;
import com.idol.board.repository.comment.CommentRepository;
import com.idol.board.repository.location.LocationRepository;
import com.idol.board.usecase.article.command.DeleteArticleUseCase;
import com.idol.global.exception.ArticleNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteArticleService implements DeleteArticleUseCase {

    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;
    private final LocationRepository locationRepository;
    @Override
    @Transactional
    public Long delete(Long articleId){
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new ArticleNotFoundException(articleId));

        commentRepository.softDeleteAllByArticleId(articleId);
        locationRepository.findById(article.getLocationId())
                .ifPresent(location -> {
                    location.markAsDeleted();
                });

        article.markAsDeleted();

        return articleId;
    }
}
