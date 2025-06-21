package com.idol.board.dto.response.mypage;

import com.idol.board.dto.response.comment.CommentQuestionResponseDto;
import com.idol.board.dto.response.comment.CommentResponseDto;

import java.util.List;

public record MyPageQuestionResponseDto(
        List<CommentQuestionResponseDto> answerWait,
        List<CommentResponseDto> answerComplete
) {
}
