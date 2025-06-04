package com.idol.board.domain.entity;

import com.idol.board.dto.request.comment.CommentCreateRequestDto;
import com.idol.board.dto.request.comment.CommentUpdateRequestDto;
import com.idol.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "comment")
@Getter
@ToString(exclude = "article")
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Where(clause = "is_deleted = false")
public class Comment extends BaseEntity {

    @Id
    @Column(name = "comment_id", nullable = false)
    private Long commentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id", nullable = false)
    private Article article;

    @Column(name = "writer_id", nullable = false)
    private Long writerId;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "parent_comment_id")
    private Long parentCommentId;

    @Column(name = "secret", nullable = false)
    private boolean secret;

//    public void setArticle(Article article) {
//        this.article = article;
//    }

    public boolean isRoot() {
        return parentCommentId.longValue() == commentId;
    }

    public void update(CommentUpdateRequestDto requestDto) {
        if(requestDto.content() != null) this.content = requestDto.content();
        this.secret = requestDto.secret();
    }
}
