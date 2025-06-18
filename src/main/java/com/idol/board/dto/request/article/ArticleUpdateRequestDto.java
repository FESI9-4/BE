package com.idol.board.dto.request.article;

import com.idol.board.domain.SmallCategory;

import java.sql.Timestamp;

public record ArticleUpdateRequestDto(
        String title,
        String roadNameAddress,
        Double latitude,
        Double longitude,
        String imageKey,
        String description,
        SmallCategory smallCategory,
        Long date,
        Long deadline,
        Integer minPerson,
        Integer maxPerson
) {
}
