package com.idol.board.repository.mapper;

import java.sql.Timestamp;

public record CommentReadDao (
        Long commentId,
        String content,
        Long parentCommentId,
        Long writerId,
        boolean isDeleted,
        Timestamp createdAt,
        boolean secret
){
}
