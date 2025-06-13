package com.idol.board.repository.comment;

import com.idol.board.domain.entity.Comment;
import com.idol.board.domain.entity.QComment;
import com.idol.board.repository.mapper.CommentReadQueryResult;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
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
    private static final QComment comment = QComment.comment;

    @Override
    public Optional<Comment> findByCommentId(Long commentId) {

        return Optional.ofNullable(
                queryFactory
                        .selectFrom(comment)
                        .where(
                                comment.isDeleted.eq(false),
                                comment.commentId.eq(commentId)
                        )
                        .fetchOne()
        );
    }


//    @Override
//    public Optional<Comment> findByParentCommentId(Long parentCommentId) {
//        String sql = "select a from Comment a where a.isDeleted = false and a.parentCommentId = :parentCommentId";
//        Query query = entityManager.createQuery(sql)
//                .setParameter("parentCommentId", parentCommentId);
//        try {
//            return Optional.ofNullable((Comment) query.getSingleResult());
//        } catch (NoResultException e) {
//            return Optional.empty();
//        }
//    }


    @Override
    public Optional<Comment> findByParentCommentId(Long parentCommentId) {

        return Optional.ofNullable(
                queryFactory
                .selectFrom(comment)
                .where(
                        comment.isDeleted.eq(false),
                        comment.parentCommentId.eq(parentCommentId)
                )
                .fetchOne()
        );
    }

    // 연관된 댓글 개수 확인
//    @Override
//    public Long relatedCommentCountBy(Long articleId, Long parentCommentId, Long limit) {
//        String sql = "select count(*) from(" +
//                "   select comment_id from comment" +
//                "   where is_deleted = false and article_id = :articleId and parent_comment_id = :parentCommentId" +
//                "   limit :limit" +
//                ") t";
//
//        Query query = entityManager.createNativeQuery(sql)
//                .setParameter("articleId", articleId)
//                .setParameter("parentCommentId", parentCommentId)
//                .setParameter("limit", limit);
//
//
//        log.info(String.valueOf(((Number) query.getSingleResult()).longValue()));
//        return ((Number) query.getSingleResult()).longValue();
//    }

    @Override
    public Long relatedCommentCountBy(Long articleId, Long parentCommentId, Long limit) {
        return queryFactory
                .select(comment.count())
                .from(comment)
                .where(
                        comment.isDeleted.eq(false),
                        comment.articleId.eq(articleId),
                        comment.parentCommentId.eq(parentCommentId)
                )
                .limit(limit)
                .fetchOne();
    }

    // 맨 처음 데이터 출력
    @Override
    public List<CommentReadQueryResult> findAllInfiniteScroll(Long articleId, Long limit) {

//        QComment comment = QComment.comment;
        return queryFactory
                .select(Projections.constructor(CommentReadQueryResult.class,
                        comment.commentId,
                        comment.content,
                        comment.parentCommentId,
                        comment.writerId,
                        comment.isDeleted,
                        comment.createdAt,
                        comment.secret))
                .from(comment)
                .where(
                        comment.isDeleted.eq(false),
                        comment.articleId.eq(articleId)
                )
                .orderBy(
                        comment.parentCommentId.asc(),
                        comment.commentId.asc()
                )
                .limit(limit)
                .fetch();
    }


    // 스크롤 n 페이지 데이터 출력
    // 무한 스크롤일때는 출력되는 화면에 대댓글 중 일부가 안보이는 경우가 발생함 따라서,
    //                     "   parent_comment_id > :lastParentCommentId or " +
    //                    "   (parent_comment_id = :lastParentCommentId and comment_id > :lastCommentId) " +
    // 해당 코드에서 parent_comment_id값이 출력되는 댓글의 마지막 값인 lastParentCommentId보다 같으면서 commentId값이 lastCommentId보다 큰 경우를 찾아 대댓글까지 출력시킴
    @Override
    public List<CommentReadQueryResult> findAllInfiniteScroll(Long articleId, Long lastParentCommentId, Long lastCommentId, Long limit) {
//        QComment comment = QComment.comment;

        return queryFactory
                .select(Projections.constructor(CommentReadQueryResult.class,
                        comment.commentId,
                        comment.content,
                        comment.parentCommentId,
                        comment.writerId,
                        comment.isDeleted,
                        comment.createdAt,  // LocalDateTime 타입
                        comment.secret))
                .from(comment)
                .where(
                        comment.isDeleted.eq(false),
                        comment.articleId.eq(articleId),
                        new BooleanBuilder()
                                .or(comment.parentCommentId.gt(lastParentCommentId))
                                .or(new BooleanBuilder()
                                        .and(comment.parentCommentId.eq(lastParentCommentId))
                                        .and(comment.commentId.gt(lastCommentId)))
                )
                .orderBy(
                        comment.parentCommentId.asc(),
                        comment.commentId.asc()
                )
                .limit(limit)
                .fetch();
    }
}
