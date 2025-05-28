package com.idol.board.dto.request.article;

import com.idol.board.domain.BigCategory;
import com.idol.board.domain.SmallCategory;
import com.idol.board.domain.entity.Article;
import com.idol.board.domain.entity.Location;

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


    public Article toEntity(Long writerId) {
        Location location = Location.builder()
                .roadNameAddress(roadNameAddress)
                .latitude(latitude)
                .longitude(longitude)
                .build();

        return Article.builder()
                .writerId(writerId)
                .title(title)
                .location(location)
                .articleImageKey(imageKey)
                .description(description)
                .bigCategory(SPECIAL_CATEGORIES.contains(smallCategory) ? BigCategory.GO_TYPE : BigCategory.DOING_TYPE)
                .smallCategory(smallCategory)
                .date(date)
                .deadline(deadline)
                .minPerson(minPerson)
                .maxPerson(maxPerson)
                .currentPerson(1)
                .openStatus(PENDING_STATUS)
                .useStatus(UPCOMING_STATUS)
                .build();
    }
}