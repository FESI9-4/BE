package com.idol.board.dto.request.article;

import com.idol.board.domain.BigCategory;
import com.idol.board.domain.SmallCategory;
import com.idol.board.domain.entity.Article;

import java.sql.Timestamp;
import java.util.Set;

import static com.idol.board.domain.OpenStatus.PENDING_STATUS;
import static com.idol.board.domain.UseStatus.UPCOMING_STATUS;

public record ArticleCreateRequestDto(
        String title,
        String roadNameAddress,
        double latitude,
        double longitude,
        String imageKey,
        String description,
        SmallCategory smallCategory,
        Timestamp date,
        Timestamp deadline,
        int minPerson,
        int maxPerson
) {

    private static final Set<SmallCategory> SPECIAL_CATEGORIES = Set.of(
            SmallCategory.BUSRENTAL_TYPE,
            SmallCategory.COMPANION_TYPE,
            SmallCategory.AFTERPARTY_TYPE
    );


    public Article toEntity(Long writerId, Long locationId) {
        return new Article(
                writerId,
                title,
                locationId,
                SPECIAL_CATEGORIES.contains(smallCategory) ? BigCategory.GO_TYPE : BigCategory.DOING_TYPE,
                smallCategory,
                description,
                date,
                deadline,
                minPerson,
                maxPerson,
                imageKey,
                PENDING_STATUS,
                UPCOMING_STATUS);
    }
}
