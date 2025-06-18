package com.idol.board.service.article.command;

import com.idol.board.domain.entity.Article;
import com.idol.board.domain.entity.Location;
import com.idol.board.dto.request.article.ArticleUpdateRequestDto;
import com.idol.board.repository.article.ArticleRepository;
import com.idol.board.repository.location.LocationRepository;
import com.idol.board.usecase.article.command.UpdateArticleUseCase;
import com.idol.global.exception.IllegalArgumentException;
import com.idol.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static java.util.function.Predicate.not;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UpdateArticleService implements UpdateArticleUseCase {

    private final ArticleRepository articleRepository;
    private final LocationRepository locationRepository;

    @Override
    public Long updateArticle(ArticleUpdateRequestDto requestDto, Long writerId, Long articleId) {
        Article article = articleRepository.findByArticleId(articleId)
                .filter(not(Article::getIsDeleted))
                .orElseThrow(() -> new NotFoundException("Article", articleId));

        validateUserHasPermission(article, writerId);

        // Article 필드 업데이트
        article.update(requestDto);

        // Location 업데이트
        Location location = locationRepository.findByLocationId(article.getLocationId())
                .orElseThrow(() -> new NotFoundException("Article", articleId));

        location.update(requestDto);
        return article.getArticleId();
    }

    private void validateUserHasPermission(Article article, Long writerId) {
        if (article.getWriterId() != writerId) {
            log.info(String.valueOf(article.getWriterId() == writerId));
            throw new IllegalArgumentException("Article",article.getArticleId());
        }
    }
}
