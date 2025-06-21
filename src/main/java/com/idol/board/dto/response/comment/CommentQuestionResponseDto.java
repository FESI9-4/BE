package com.idol.board.dto.response.comment;

import com.idol.board.domain.entity.Article;
import com.idol.board.repository.mapper.CommentReadQueryResult;
import com.idol.board.repository.mapper.CommentReadQuestionQueryResult;

import java.sql.Timestamp;

public record CommentQuestionResponseDto(
        Long commentId,
        String content,
        Long parentCommentId,
        Long writerId,
        boolean deleted,
        Long createdAt,
        boolean secret,
        String nickName,
        String writerImageUrl,
        Long articleId,
        String title,
        String locationAddress
) {

    public static Long changeToUnixTime(Timestamp ts) {
        // 2. 밀리초 단위 UNIX timestamp 얻기
        long unixMillis = ts.getTime(); // 1750288750000

        // 3. 초 단위 UNIX timestamp 얻기
        long unixSeconds = unixMillis / 1000; // 1750288750

        return unixSeconds;
    }

    public static CommentQuestionResponseDto from(CommentReadQuestionQueryResult result, String nickName, String writerImageUrl, Article article) {
        return new CommentQuestionResponseDto(
                result.commentId(),
                result.content(),
                result.parentCommentId(),
                result.writerId(),
                result.isDeleted(),
                changeToUnixTime(Timestamp.valueOf(result.createdAt())),
                result.secret(),
                nickName,
                writerImageUrl,
                article.getArticleId(),
                article.getTitle(),
                article.getLocationAddress());
    }
}
