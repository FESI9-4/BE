package com.idol.board.repository.mapper;

import com.idol.board.domain.OpenStatus;
import com.idol.board.domain.UseStatus;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public record ArticleImgListReadQueryResult(
        Long articleId,
        String title,
        Long locationId,
        String imageKey,
        Timestamp date,
        Timestamp deadLine,
        LocalDateTime createAt,
        int currentPerson,
        int maxPerson,
        OpenStatus openStatus,
        UseStatus useStatus,
        Long writerId
) {
}
