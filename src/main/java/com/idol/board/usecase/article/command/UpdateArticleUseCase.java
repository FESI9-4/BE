package com.idol.board.usecase.article.command;

import com.idol.board.dto.request.article.ArticleUpdateRequestDto;

public interface UpdateArticleUseCase {
    Long updateArticle(ArticleUpdateRequestDto requestDto, Long writerId  ,Long articleId);
}
