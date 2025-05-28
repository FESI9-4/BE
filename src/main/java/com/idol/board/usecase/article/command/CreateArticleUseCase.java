package com.idol.board.usecase.article.command;

import com.idol.board.dto.request.article.ArticleCreateRequestDto;

public interface CreateArticleUseCase {
    Long createArticle(ArticleCreateRequestDto requestDto, Long writerId);
}
