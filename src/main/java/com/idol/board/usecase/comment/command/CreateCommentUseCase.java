package com.idol.board.usecase.comment.command;

import com.idol.board.dto.request.comment.CommentCreateRequestDto;

public interface CreateCommentUseCase {
    Long createComment(CommentCreateRequestDto requestDto, Long writerId, Long articleId);
}
