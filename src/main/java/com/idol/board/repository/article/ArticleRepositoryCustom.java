package com.idol.board.repository.article;

import com.idol.board.domain.BigCategory;
import com.idol.board.domain.SmallCategory;
import com.idol.board.domain.entity.Article;
import com.idol.board.dto.response.article.ArticleListResponseDto;
import com.idol.board.repository.mapper.ArticleImgListReadQueryResult;
import com.idol.board.repository.mapper.ArticleListReadQueryResult;
import com.idol.board.repository.mapper.ArticleReadAnswerQueryResult;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;


public interface ArticleRepositoryCustom {
    Optional<Article> findByArticleId(Long articleId);

    List<ArticleImgListReadQueryResult> findArticleList(
            BigCategory bigCategory, SmallCategory smallCategory, String location,
            Timestamp date, String sort, boolean sortAsc, Long limit, Long offset);

    List<ArticleListReadQueryResult> findMyPageArticle(Long userId, Long limit, Long offset);

    List<ArticleListReadQueryResult> findJoinMyPageArticle(List<Long> articleIds, Long limit, Long offset);

    List<ArticleReadAnswerQueryResult> findAllByWriterIdInfiniteScrollFromArticle(Long userId, Long limit);
    List<ArticleReadAnswerQueryResult> findAllByWriterIdInfiniteScrollFromArticle(Long lastArticleId,   Long userId, Long limit);
}
