package com.idol.board.usecase.article.command;

public interface ParticipantUseCase {
    Long joinFanFal(Long articleId, Long writerId);

    Long cancelFanFal(Long articleId, Long writerId);
}
