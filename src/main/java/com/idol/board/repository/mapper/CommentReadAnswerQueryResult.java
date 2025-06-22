package com.idol.board.repository.mapper;

import java.time.LocalDateTime;

public record CommentReadAnswerQueryResult(
        String content,
        LocalDateTime createdAt,
        Long commentId,
        Long parentCommentId
) {
}
