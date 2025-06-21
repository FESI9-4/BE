package com.idol.domains.wish.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Builder
public record WishLikeRequestDto(
        @Schema(description = "게시물 id 리스트", example = "[1, 2, 3, 4]")
        @NotNull(message = "게시물 id를 입력해주세요.")
        List<Long> articleIds
) {
}
