package com.idol.board.dto.response.mypage;

import java.util.List;

public record UserAnswerTotalResponseDto<T>(
        int totalCount,
        List<T> data
) {
}
