package com.idol.board.dto.response.mypage;

import java.util.List;

public record UserAnswerTotalResponseDto(
        int totalCount,
        List<UserAnswerResponseDto> data
) {
}
