package com.idol.board.dto.request.comment;

import com.idol.board.domain.entity.Article;
import com.idol.board.domain.entity.Comment;

public record CommentCreateRequestDto(
        String content,
        Long parentCommentId,
        boolean secret
) {

    public Comment toEntity(Long commentId, Article article, Long writerId, Long parentCommentId) {
        return Comment.builder()
                .commentId(commentId)
                .content(this.content)
                .parentCommentId(parentCommentId == null ? commentId : parentCommentId)
                .article(article)
                .writerId(writerId)
                .secret(this.secret)
                .build();
    }
}
