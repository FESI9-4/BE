package com.idol.board.usecase.comment.command;

import com.idol.board.dto.request.comment.CommentCreateRequestDto;
import com.idol.board.dto.request.comment.CommentUpdateRequestDto;

public interface UpdateCommentUseCase {
    Long updateComment(CommentUpdateRequestDto  commentUpdateRequestDto,Long writerId);
}
