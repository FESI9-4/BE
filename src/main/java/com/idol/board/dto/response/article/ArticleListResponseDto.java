package com.idol.board.dto.response.article;

import com.idol.board.domain.BigCategory;
import com.idol.board.domain.OpenStatus;
import com.idol.board.domain.SmallCategory;
import com.idol.board.domain.UseStatus;
import com.idol.board.domain.entity.Article;
import com.idol.board.repository.mapper.ArticleListReadQueryResult;
import lombok.Builder;

import java.sql.Timestamp;

@Builder
public record ArticleListResponseDto(
    Long articleId,
    String title,
    String location,
    Timestamp date,
    Timestamp deadLine,
    Timestamp createAt,
    int currentPerson,
    int maxPerson,
    OpenStatus openStatus,
    boolean wish,
    String image,
    UseStatus useStatus
) {

    public static ArticleListResponseDto from(ArticleListReadQueryResult searchArticle, String location, String imageUrl) {
        return ArticleListResponseDto.builder()
                .articleId(searchArticle.articleId())
                .title(searchArticle.title())
                .location(location)
                .date(searchArticle.date())
                .deadLine(searchArticle.deadLine())
                .createAt(Timestamp.valueOf(searchArticle.createAt()))
                .currentPerson(searchArticle.currentPerson())
                .maxPerson(searchArticle.maxPerson())
                .openStatus(searchArticle.openStatus())
                .wish(true)             // TODO :: Wish 연동되면 설정 추가
                .image(imageUrl)
                .useStatus(searchArticle.useStatus())
                .build();
    }
}

