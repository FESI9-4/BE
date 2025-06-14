package com.idol.board.dto.request.comment;

import com.idol.board.domain.entity.Article;
import com.idol.board.domain.entity.Comment;
import lombok.Builder;

public record CommentCreateRequestDto(
        String content,
        Long parentCommentId,
        boolean secret
) {

    public Comment toEntity(Long commentId, Long articleId, Long writerId, Long parentCommentId) {
        return Comment.builder()
                .commentId(commentId)
                .articleId(articleId)
                .writerId(writerId)
                .content(this.content)
                .parentCommentId(parentCommentId == null ? commentId : parentCommentId)
                .secret(this.secret)
                .build();
    }
}
