package com.idol.board.dto.response.mypage;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public record UserAnswerResponseDto(
        Long articleId,
        String title,
        String location,
        Long createdAt,
        String comment,
        boolean answer
) {
    public static Long changeToUnixTime(Timestamp ts) {
        // 2. 밀리초 단위 UNIX timestamp 얻기
        long unixMillis = ts.getTime(); // 1750288750000

        // 3. 초 단위 UNIX timestamp 얻기
        long unixSeconds = unixMillis / 1000; // 1750288750

        return unixSeconds;
    }

    public static UserAnswerResponseDto from(        Long articleId,
                                              String title,
                                              String location,
                                              LocalDateTime createdAt,
                                              String comment,
                                              boolean answer){



        return new UserAnswerResponseDto(articleId, title, location, changeToUnixTime(Timestamp.valueOf(createdAt)), comment, answer);
    }
}
