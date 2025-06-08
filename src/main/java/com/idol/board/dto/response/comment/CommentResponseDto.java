package com.idol.board.dto.response.comment;

import com.idol.board.repository.mapper.CommentReadQueryResult;

import java.sql.Timestamp;

public record CommentResponseDto(
        Long commentId,
        String content,
        Long parentCommentId,
        Long writerId,
        boolean deleted,
        Timestamp createdAt,
        boolean secret
) {

    public static CommentResponseDto from(CommentReadQueryResult result){
        return new CommentResponseDto(
                result.commentId(),
                result.content(),
                result.parentCommentId(),
                result.writerId(),
                result.isDeleted(),
                Timestamp.valueOf(result.createdAt()),
                result.secret());
    }
}
