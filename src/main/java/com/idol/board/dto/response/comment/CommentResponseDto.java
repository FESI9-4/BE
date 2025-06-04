package com.idol.board.dto.response.comment;

import com.idol.board.domain.entity.Comment;
import com.idol.board.repository.mapper.CommentReadDao;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public record CommentResponseDto(
        Long commentId,
        String content,
        Long parentCommentId,
        Long writerId,
        boolean deleted,
        Timestamp createdAt,
        boolean secret
) {

    public static CommentResponseDto from(CommentReadDao dao){
        return new CommentResponseDto(
                dao.commentId(),
                dao.content(),
                dao.parentCommentId(),
                dao.writerId(),
                dao.isDeleted(),
                dao.createdAt(),
                dao.secret());
    }
}
