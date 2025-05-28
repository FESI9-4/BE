package com.idol.board.domain.entity;

import com.idol.board.dto.request.comment.CommentCreateRequestDto;
import com.idol.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "comment")
@Getter
@Builder
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Where(clause = "is_deleted = false")
public class Comment extends BaseEntity {

    @Id
    @Column(name = "comment_id", nullable = false)
    private Long commentId;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "parent_comment_id", nullable = true)
    private Long parentCommentId;

    @Column(name = "parent_comment_delete_status", nullable = true)
    private Long parentCommentDeleteStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    private Article article;

    @Column(name = "writer_id", nullable = false)
    private Long writerId;

    @Column(name = "secret", nullable = false)
    private boolean secret;


    // 현재 댓글이 최상위 댓글인지 확인
    public boolean isRoot(){
        return parentCommentId.longValue() == commentId;
    }


    public Comment create(Article article, Long writerId, Long parentCommentId) {
        return Comment.builder()
                .content(this.content)
                .parentCommentId(parentCommentId == null ? commentId : parentCommentId)
                .article(article)
                .writerId(writerId)
                .secret(this.secret)
                .build();
    }
}
