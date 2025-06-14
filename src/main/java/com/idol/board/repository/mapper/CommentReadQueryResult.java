package com.idol.board.repository.mapper;

import java.time.LocalDateTime;

public record CommentReadQueryResult(
        Long commentId,
        String content,
        Long parentCommentId,
        Long writerId,
        boolean isDeleted,
        LocalDateTime createdAt,
        boolean secret
){
}
