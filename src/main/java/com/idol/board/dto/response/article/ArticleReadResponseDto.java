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
        Timestamp date,
        Timestamp deadLine,
        Timestamp createdAt,
        int min_person,
        int currentPerson,
        int maxPerson,
        List<ParticipantResponseDto> participants,
        boolean wishList,
        String articleImageUrl,
        OpenStatus openStatus,
        UseStatus useStatus
        ){


        public static ArticleReadResponseDto from(Article article, Location location, List<ParticipantResponseDto> participants, boolean wishList,String articleImageUrl){
                return new ArticleReadResponseDto(
                        article.getTitle(),
                        location.getRoadNameAddress(),
                        location.getLatitude(),
                        location.getLongitude(),
                        article.getDescription(),
                        article.getDate(),
                        article.getDeadline(),
                        Timestamp.valueOf(article.getCreatedAt()),
                        article.getMinPerson(),
                        article.getCurrentPerson(),
                        article.getMaxPerson(),
                        participants,
                        wishList,
                        articleImageUrl,
                        article.getOpenStatus(),
                        article.getUseStatus()
                );
        }
}