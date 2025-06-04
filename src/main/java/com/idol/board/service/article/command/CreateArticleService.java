package com.idol.board.service.article.command;

import com.idol.board.domain.entity.Article;
import com.idol.board.dto.request.article.ArticleCreateRequestDto;
import com.idol.board.repository.article.ArticleRepository;
import com.idol.board.usecase.article.command.CreateArticleUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateArticleService implements CreateArticleUseCase {

    private final ArticleRepository articleJpaRepository;

    @Override
    @Transactional
    public Long createArticle(ArticleCreateRequestDto requestDto, Long writerId) {
        Article article = requestDto.toEntity(writerId);
        articleJpaRepository.save(article);

        return article.getArticleId();
    }
}
