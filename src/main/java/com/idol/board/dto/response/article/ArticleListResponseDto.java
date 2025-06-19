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
    Long date,
    Long deadLine,
    Long createAt,
    int currentPerson,
    int maxPerson,
    OpenStatus openStatus,
    boolean wish,
    String image,
    UseStatus useStatus
) {

    public static Long changeToUnixTime(Timestamp ts) {
        // 2. 밀리초 단위 UNIX timestamp 얻기
        long unixMillis = ts.getTime(); // 1750288750000

        // 3. 초 단위 UNIX timestamp 얻기
        long unixSeconds = unixMillis / 1000; // 1750288750

        return unixSeconds;
    }


    public static ArticleListResponseDto from(ArticleListReadQueryResult searchArticle, String location, String imageUrl) {
        return ArticleListResponseDto.builder()
                .articleId(searchArticle.articleId())
                .title(searchArticle.title())
                .location(location)
                .date(changeToUnixTime(searchArticle.date()))
                .deadLine(changeToUnixTime(searchArticle.deadLine()))
                .createAt(changeToUnixTime(Timestamp.valueOf(searchArticle.createAt())))
                .currentPerson(searchArticle.currentPerson())
                .maxPerson(searchArticle.maxPerson())
                .openStatus(searchArticle.openStatus())
                .wish(true)             // TODO :: Wish 연동되면 설정 추가
                .image(imageUrl)
                .useStatus(searchArticle.useStatus())
                .build();
    }
}

