package com.idol.board.repository.comment;

import com.idol.board.domain.entity.Comment;
import com.idol.board.repository.mapper.CommentReadQueryResult;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class CommentRepositoryImpl implements CommentRepositoryCustom {

    private final EntityManager entityManager;
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Comment> findByCommentId(Long commentId) {
        String sql = "select a from Comment a where a.isDeleted = false and a.commentId = :commentId";
        Query query = entityManager.createQuery(sql)
                .setParameter("commentId", commentId);
        try {
            return Optional.ofNullable((Comment) query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Comment> findByParentCommentId(Long parentCommentId) {
        String sql = "select a from Comment a where a.isDeleted = false and a.parentCommentId = :parentCommentId";
        Query query = entityManager.createQuery(sql)
                .setParameter("parentCommentId", parentCommentId);
        try {
            return Optional.ofNullable((Comment) query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }


    // 연관된 댓글 개수 확인
    @Override
    public Long relatedCommentcountBy(Long articleId, Long parentCommentId, Long limit) {
        String sql = "select count(*) from(" +
                "   select comment_id from comment" +
                "   where is_deleted = false and article_id = :articleId and parent_comment_id = :parentCommentId" +
                "   limit :limit" +
                ") t";

        Query query = entityManager.createNativeQuery(sql)
                .setParameter("articleId", articleId)
                .setParameter("parentCommentId", parentCommentId)
                .setParameter("limit", limit);


        log.info(String.valueOf(((Number) query.getSingleResult()).longValue()));
        return ((Number) query.getSingleResult()).longValue();
    }

    @Override
    public void softDeleteAllByArticleId(Long articleId) {
        String sql = "UPDATE Comment c SET c.isDeleted = true WHERE c.articleId = :articleId";
        Query query = entityManager.createQuery(sql)
                .setParameter("articleId", articleId);
        query.executeUpdate();
    }


    // 맨 처음 데이터 출력
    @Override
    public List<CommentReadQueryResult> findAllInfiniteScroll(Long articleId, Long limit) {

        String sql = "SELECT comment_id ,content , parent_comment_id, " +
                "writer_id, is_deleted, created_at , secret " +
                "from comment " +
                "where is_deleted = false and article_id = ?1 " +
                "order by parent_comment_id asc, comment_id asc " +
                "limit ?2";  // 위치 기반 파라미터로 변경

        Query query = entityManager.createNativeQuery(sql, CommentReadQueryResult.class)
                .setParameter(1, articleId)
                .setParameter(2, limit);

        return query.getResultList();
    }



    // 스크롤 n 페이지 데이터 출력
    // 무한 스크롤일때는 출력되는 화면에 대댓글 중 일부가 안보이는 경우가 발생함 따라서,
    //                     "   parent_comment_id > :lastParentCommentId or " +
    //                    "   (parent_comment_id = :lastParentCommentId and comment_id > :lastCommentId) " +
    // 해당 코드에서 parent_comment_id값이 출력되는 댓글의 마지막 값인 lastParentCommentId보다 같으면서 commentId값이 lastCommentId보다 큰 경우를 찾아 대댓글까지 출력시킴
    @Override
    public List<CommentReadQueryResult> findAllInfiniteScroll(Long articleId, Long lastParentCommentId, Long lastCommentId, Long limit) {
        String sql = "SELECT comment_id ,content , parent_comment_id, " +
                "writer_id, is_deleted, created_at , secret " +
                "from comment " +
                "where is_deleted = false and article_id = ?1 and (" +
                "   parent_comment_id > ?2 or " +
                "   (parent_comment_id = ?2 and comment_id > ?3) " +
                ")" +
                "order by parent_comment_id asc, comment_id asc " +
                "limit ?4";

        Query query = entityManager.createNativeQuery(sql, CommentReadQueryResult.class)
                .setParameter(1, articleId)
                .setParameter(2, lastParentCommentId)
                .setParameter(3, lastCommentId)
                .setParameter(4, limit);

        return query.getResultList();
    }
}
