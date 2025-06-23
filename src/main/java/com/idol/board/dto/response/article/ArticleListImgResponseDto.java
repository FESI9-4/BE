package com.idol.board.dto.response.article;

import com.idol.board.domain.OpenStatus;
import com.idol.board.domain.UseStatus;
import com.idol.board.repository.mapper.ArticleImgListReadQueryResult;
import com.idol.board.repository.mapper.ArticleListReadQueryResult;
import lombok.Builder;

import java.sql.Timestamp;

@Builder
public record ArticleListImgResponseDto(
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
        UseStatus useStatus,
        String nickName,
        String writerImageUrl
) {

    public static Long changeToUnixTime(Timestamp ts) {
        // 2. 밀리초 단위 UNIX timestamp 얻기
        long unixMillis = ts.getTime(); // 1750288750000

        // 3. 초 단위 UNIX timestamp 얻기
        long unixSeconds = unixMillis / 1000; // 1750288750

        return unixSeconds;
    }


    public static ArticleListImgResponseDto from(ArticleImgListReadQueryResult searchArticle, String location, OpenStatus status, boolean wish,String imageUrl, String nickName, String writerImageUrl) {
        return ArticleListImgResponseDto.builder()
                .articleId(searchArticle.articleId())
                .title(searchArticle.title())
                .location(location)
                .date(changeToUnixTime(searchArticle.date()))
                .deadLine(changeToUnixTime(searchArticle.deadLine()))
                .createAt(changeToUnixTime(Timestamp.valueOf(searchArticle.createAt())))
                .currentPerson(searchArticle.currentPerson())
                .maxPerson(searchArticle.maxPerson())
                .openStatus(status)
                .wish(wish)             // TODO :: Wish 연동되면 설정 추가
                .image(imageUrl)
                .useStatus(searchArticle.useStatus())
                .nickName(nickName)
                .writerImageUrl(writerImageUrl)
                .build();
    }
}

