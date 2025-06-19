package com.idol.board.domain.entity;

import com.idol.board.dto.request.comment.CommentCreateRequestDto;
import com.idol.board.dto.request.comment.CommentUpdateRequestDto;
import com.idol.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.*;

@Entity
@Table(name = "comment")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {

    @Id
    @Column(name = "comment_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @Column(name = "article_id", nullable = false)
    private Long articleId;

    @Column(name = "writer_id", nullable = false)
    private Long writerId;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "parent_comment_id")
    private Long parentCommentId;

    @Column(name = "secret", nullable = false)
    private boolean secret;

    @Builder
    public Comment(Long commentId, Long articleId, Long writerId, String content, Long parentCommentId, boolean secret) {
        this.commentId = commentId;
        this.articleId = articleId;
        this.writerId = writerId;
        this.content = content;
        this.parentCommentId = parentCommentId;
        this.secret = secret;
    }

    public void addCommentParentId(){
        this.parentCommentId = this.getCommentId();
    }

    public boolean isRoot() {
        return parentCommentId.longValue() == commentId;
    }

    public void update(CommentUpdateRequestDto requestDto) {
        if(requestDto.content() != null) this.content = requestDto.content();
        this.secret = requestDto.secret();
    }
}
