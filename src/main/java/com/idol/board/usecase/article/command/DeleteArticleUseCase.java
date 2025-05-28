package com.idol.board.usecase.article.command;

import jakarta.transaction.Transactional;

public interface DeleteArticleUseCase {
    Long delete(Long articleId);
}
