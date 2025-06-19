package com.idol.board.dto.response.article;

import com.idol.board.domain.OpenStatus;
import com.idol.board.domain.UseStatus;
import com.idol.board.domain.entity.Article;
import com.idol.board.domain.entity.Location;
import com.idol.board.domain.entity.Participant;
import com.idol.board.dto.response.participant.ParticipantResponseDto;

import java.sql.Timestamp;
import java.util.List;

public record ArticleReadResponseDto(
        String title,
        String location,
        double latitude,
        double longitude,
        String description,
        Long date,
        Long deadLine,
        Long createdAt,
        int min_person,
        int currentPerson,
        int maxPerson,
        List<ParticipantResponseDto> participants,
        boolean wishList,
        String articleImageUrl,
        OpenStatus openStatus,
        UseStatus useStatus,
        String nickName,
        String writerImageUrl
        ){

        public static Long changeToUnixTime(Timestamp ts) {
                // 2. 밀리초 단위 UNIX timestamp 얻기
                long unixMillis = ts.getTime(); // 1750288750000

                // 3. 초 단위 UNIX timestamp 얻기
                long unixSeconds = unixMillis / 1000; // 1750288750

                return unixSeconds;
        }

        public static ArticleReadResponseDto from(Article article, Location location, List<ParticipantResponseDto> participants, boolean wishList,String articleImageUrl, String nickName, String writerImageUrl){
                return new ArticleReadResponseDto(
                        article.getTitle(),
                        location.getRoadNameAddress(),
                        location.getLatitude(),
                        location.getLongitude(),
                        article.getDescription(),
                        changeToUnixTime(article.getDate()),
                        changeToUnixTime(article.getDeadline()),
                        changeToUnixTime(Timestamp.valueOf(article.getCreatedAt())),
                        article.getMinPerson(),
                        article.getCurrentPerson(),
                        article.getMaxPerson(),
                        participants,
                        wishList,
                        articleImageUrl,
                        article.getOpenStatus(),
                        article.getUseStatus(),
                        nickName,
                        writerImageUrl
                );
        }
}