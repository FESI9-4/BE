package com.idol.domains.wish.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record GetMembersWishListResponseDto(
    Long wishId,
    Long articleId
) {
}
