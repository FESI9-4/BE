package com.idol.board.dto.request.article;

import com.idol.board.domain.BigCategory;
import com.idol.board.domain.OpenStatus;
import com.idol.board.domain.SmallCategory;
import com.idol.board.domain.UseStatus;
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
        Long date,
        Long deadline,
        int minPerson,
        int maxPerson
) {

    private static final Set<SmallCategory> SPECIAL_CATEGORIES = Set.of(
            SmallCategory.BUSRENTAL_TYPE,
            SmallCategory.COMPANION_TYPE,
            SmallCategory.AFTERPARTY_TYPE
    );

    private Timestamp changeTimeStamp(Long time){
        return new Timestamp(time * 1000); // 밀리초 단위로 변환
    }

    public Article toEntity(Long writerId, Long locationId, String locationAddress) {
        return Article.builder()
                .writerId(writerId)
                .title(title)
                .locationId(locationId)
                .locationAddress(locationAddress)
                .bigCategory(SPECIAL_CATEGORIES.contains(smallCategory) ? BigCategory.GO_TYPE : BigCategory.DOING_TYPE)
                .smallCategory(smallCategory)
                .description(description)
                .date(changeTimeStamp(date))
                .deadline(changeTimeStamp(deadline))
                .minPerson(minPerson)
                .maxPerson(maxPerson)
                .articleImageKey(imageKey)
                .openStatus(PENDING_STATUS)
                .useStatus(UPCOMING_STATUS)
                .build();
    }
}
