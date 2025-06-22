package com.idol.board.repository.comment;

import com.idol.board.domain.entity.Comment;
import com.idol.board.repository.mapper.CommentReadAnswerQueryResult;
import com.idol.board.repository.mapper.CommentReadQueryResult;
import com.idol.board.repository.mapper.CommentReadQuestionQueryResult;

import java.util.List;
import java.util.Optional;


public interface CommentRepositoryCustom {

    Optional<Comment> findByCommentId(Long articleId);

    Long relatedCommentCountBy(Long articleId, Long parentCommentId, Long limit);

    List<CommentReadQueryResult> findAllInfiniteScroll(Long articleId, Long limit);
    List<CommentReadQueryResult> findAllInfiniteScroll(Long articleId, Long lastParentCommentId, Long lastCommentId, Long limit);

    Optional<Comment> findByParentCommentId(Long parentCommentId);

    List<CommentReadAnswerQueryResult> findAllByArticleId(Long articleId);

    Long parentIdCountBy(Long parentCommentId);
}
