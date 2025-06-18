package com.idol.board.dto.response.comment;

import com.idol.board.repository.mapper.CommentReadQueryResult;

import java.sql.Timestamp;

public record CommentResponseDto(
        Long commentId,
        String content,
        Long parentCommentId,
        Long writerId,
        boolean deleted,
        Long createdAt,
        boolean secret,
        String nickName,
        String writerImageUrl
) {

    public static Long changeToUnixTime(Timestamp ts) {
        // 2. 밀리초 단위 UNIX timestamp 얻기
        long unixMillis = ts.getTime(); // 1750288750000

        // 3. 초 단위 UNIX timestamp 얻기
        long unixSeconds = unixMillis / 1000; // 1750288750

        return unixSeconds;
    }

    public static CommentResponseDto from(CommentReadQueryResult result,String nickName, String writerImageUrl) {
        return new CommentResponseDto(
                result.commentId(),
                result.content(),
                result.parentCommentId(),
                result.writerId(),
                result.isDeleted(),
                changeToUnixTime(Timestamp.valueOf(result.createdAt())),
                result.secret(),
                nickName,
                writerImageUrl);
    }
}
