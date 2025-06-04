package com.idol.board.dto.request.comment;

public record CommentUpdateRequestDto(
        Long commentId,
        boolean secret,
        String content
) {
}
