package com.idol.board.usecase.article.query;

import com.idol.board.domain.entity.Article;
import com.idol.board.dto.response.article.ArticleReadResponseDto;
import com.idol.board.dto.response.participant.ParticipantResponseDto;

public interface ReadArticleUseCase {
    ArticleReadResponseDto readArticle(Long articleId);
}
