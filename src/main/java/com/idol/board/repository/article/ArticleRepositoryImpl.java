package com.idol.board.repository.article;

import com.idol.board.domain.BigCategory;
import com.idol.board.domain.OpenStatus;
import com.idol.board.domain.SmallCategory;
import com.idol.board.domain.UseStatus;
import com.idol.board.domain.entity.Article;
import com.idol.board.domain.entity.QArticle;
import com.idol.board.dto.response.article.ArticleListResponseDto;
import com.idol.board.repository.mapper.ArticleListReadQueryResult;
import com.idol.board.repository.mapper.CommentReadQueryResult;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ArticleRepositoryImpl implements ArticleRepositoryCustom {
    private final EntityManager entityManager;
    private final JPAQueryFactory queryFactory;
    private static final QArticle article = QArticle.article;

    @Override
    public Optional<Article> findByArticleId(Long articleId) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(article)
                        .where(
                                article.isDeleted.eq(false),
                                article.articleId.eq(articleId)
                        )
                        .fetchOne()
        );
    }

    @Override
    public List<ArticleListReadQueryResult> findArticleList(BigCategory bigCategory, SmallCategory smallCategory, String location, Timestamp date, String sort, boolean sortAsc, Long limit, Long offset) {
        return queryFactory
                .select(Projections.constructor(ArticleListReadQueryResult.class,
                        article.articleId,
                        article.title,
                        article.locationId,
                        article.articleImageKey,
                        article.date,
                        article.deadline,
                        article.createdAt,
                        article.currentPerson,
                        article.maxPerson,
                        article.openStatus,
                        article.useStatus))
                .from(article)
                .where(
                        article.isDeleted.eq(false),
                        eqBigCategory(bigCategory),
                        eqSmallCategory(smallCategory),
                        eqLocation(location),
                        eqDate(date)
                )
                .orderBy(getOrderSpecifier(sort, sortAsc))
                .limit(limit)
                .offset(offset)
                .fetch();
    }


    @Override
    public List<ArticleListReadQueryResult> findMyPageArticle(Long userId, Long limit, Long offset) {
        return queryFactory
                .select(Projections.constructor(ArticleListReadQueryResult.class,
                        article.articleId,
                        article.title,
                        article.locationId,
                        article.articleImageKey,
                        article.date,
                        article.deadline,
                        article.createdAt,
                        article.currentPerson,
                        article.maxPerson,
                        article.openStatus,
                        article.useStatus))
                .from(article)
                .where(
                        article.isDeleted.eq(false),
                        eqWriterId(userId)
                )
                .limit(limit)
                .offset(offset)
                .fetch();
    }

    @Override
    public List<ArticleListReadQueryResult> findJoinMyPageArticle(List<Long> articleIds, Long limit, Long offset) {
            return queryFactory
                    .select(Projections.constructor(ArticleListReadQueryResult.class,
                        article.articleId,
                        article.title,
                        article.locationId,
                        article.articleImageKey,
                        article.date,
                        article.deadline,
                        article.createdAt,
                        article.currentPerson,
                        article.maxPerson,
                        article.openStatus,
                        article.useStatus))

                    .from(article)
                    .where(
                            article.isDeleted.eq(false),
                            article.articleId.in(articleIds)
                    )
                    .limit(limit)
                    .offset(offset)
                    .fetch();
    }

    private BooleanExpression eqWriterId(Long userId) {
        return userId != null ? article.writerId.eq(userId) : null;
    }

    private BooleanExpression eqBigCategory(BigCategory bigCategory) {
        return bigCategory != null ? article.bigCategory.eq(bigCategory) : null;
    }

    private BooleanExpression eqSmallCategory(SmallCategory smallCategory) {
        return smallCategory != null ? article.smallCategory.eq(smallCategory) : null;
    }

    private BooleanExpression eqLocation(String location) {
        return location != null ? article.locationAddress.contains(location) : null;
    }

    private BooleanExpression eqDate(Timestamp date) {
        return date != null ? article.date.eq(date) : null;
    }

    private OrderSpecifier<?> getOrderSpecifier(String sort, boolean sortAsc) {
        Order order = sortAsc ? Order.ASC : Order.DESC;

        if (sort == null) {
            return new OrderSpecifier<>(order, article.createdAt);
        }

        return switch (sort.toLowerCase()) {
            case "recent" -> new OrderSpecifier<>(order, article.createdAt);
            case "deadline" -> new OrderSpecifier<>(order, article.deadline);
            case "currentperson" -> new OrderSpecifier<>(order, article.currentPerson);
            default -> new OrderSpecifier<>(order, article.createdAt);
        };
    }

}
