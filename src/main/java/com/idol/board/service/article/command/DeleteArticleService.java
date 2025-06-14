package com.idol.board.service.article.command;

import com.idol.board.domain.entity.Article;
import com.idol.board.domain.entity.Comment;
import com.idol.board.repository.article.ArticleRepository;
import com.idol.board.repository.comment.CommentRepository;
import com.idol.board.repository.location.LocationRepository;
import com.idol.board.usecase.article.command.DeleteArticleUseCase;
import com.idol.global.exception.IllegalArgumentException;
import com.idol.global.exception.NotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.function.Predicate.not;

@Service
@RequiredArgsConstructor
@Transactional
public class DeleteArticleService implements DeleteArticleUseCase {

    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;
    private final LocationRepository locationRepository;

    @Override
    public Long delete(Long articleId, Long writerId){


        Article article = articleRepository.findByArticleId(articleId)
                .orElseThrow(() -> new NotFoundException("Article", articleId));

        validateUserHasPermission(article, writerId);

        List<Comment> comments = commentRepository.findByArticleId(articleId);

        comments.forEach(Comment::markAsDeleted);

        locationRepository.findById(article.getLocationId())
                .ifPresent(location -> {
                    location.markAsDeleted();
                });

        article.markAsDeleted();

        return articleId;
    }

    private void validateUserHasPermission(Article article, Long writerId) {
        if (Long.compare(article.getWriterId(), writerId) == 0) {
            throw new IllegalArgumentException("Article",article.getArticleId());
        }
    }
}
