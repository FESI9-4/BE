package com.idol.board.repository.comment;

import com.idol.board.domain.entity.Comment;
import com.idol.board.repository.mapper.CommentReadDao;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepositoryCustom {
    Long countBy(Long articleId, Long parentCommentId, Long limit);

    // 부모 댓글 삭제 시 관련된 자식 댓글들도 완전 삭제
    void deleteAllChildComments(Long parentCommentId);

    void softDeleteAllByArticleId(Long articleId);

    List<CommentReadDao> findAllInfiniteScroll(Long articleId, Long limit);
    List<CommentReadDao> findAllInfiniteScroll(Long articleId, Long lastParentCommentId, Long lastCommentId, Long limit);

}
