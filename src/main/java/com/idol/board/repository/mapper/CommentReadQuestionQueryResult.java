package com.idol.board.repository.mapper;

import java.time.LocalDateTime;

public record CommentReadQuestionQueryResult(
        Long commentId,
        Long articleId,
        String content,
        Long parentCommentId,
        Long writerId,
        boolean isDeleted,
        LocalDateTime createdAt,
        boolean secret
){
}
