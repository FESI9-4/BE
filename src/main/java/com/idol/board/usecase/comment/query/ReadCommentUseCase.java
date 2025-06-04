package com.idol.board.usecase.comment.query;

import com.idol.board.dto.response.comment.CommentResponseDto;

import java.util.List;

public interface ReadCommentUseCase {
    List<CommentResponseDto> readAll(Long articleId, Long lastParentCommentId, Long lastCommentId, Long limit);
}
