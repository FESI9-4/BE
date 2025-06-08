package com.idol.board.dto.request.comment;

import com.idol.board.domain.entity.Article;
import com.idol.board.domain.entity.Comment;

public record CommentCreateRequestDto(
        String content,
        Long parentCommentId,
        boolean secret
) {

    public Comment toEntity(Long commentId, Long articleId, Long writerId, Long parentCommentId) {

        return new Comment(
                commentId,
                articleId,
                writerId,
                this.content,
                parentCommentId == null ? commentId : parentCommentId,
                this.secret
        );
    }
}
