package com.idol.board.repository.mapper;


public record ArticleReadAnswerQueryResult(
        Long articleId,
        String title,
        String locationAddress
) {
}
