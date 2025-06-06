package com.idol.board.service.article.command;

import com.idol.board.domain.entity.Article;
import com.idol.board.domain.entity.Location;
import com.idol.board.dto.request.article.ArticleUpdateRequestDto;
import com.idol.board.repository.article.ArticleRepository;
import com.idol.board.repository.location.LocationRepository;
import com.idol.board.usecase.article.command.UpdateArticleUseCase;
import com.idol.global.exception.ArticleNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateArticleService implements UpdateArticleUseCase {

    private final ArticleRepository articleRepository;
    private final LocationRepository locationRepository;

    @Override
    @Transactional
    public Long updateArticle(ArticleUpdateRequestDto requestDto, Long writerId, Long articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new ArticleNotFoundException(articleId));

        // Article 필드 업데이트
        article.update(requestDto);

        // Location 업데이트
        Location location = locationRepository.findById(article.getLocationId())
                .orElseThrow(() -> new ArticleNotFoundException(article.getLocationId()));

        location.update(requestDto);
        return article.getArticleId();
    }
}
