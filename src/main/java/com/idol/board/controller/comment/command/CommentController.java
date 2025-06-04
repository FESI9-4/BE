package com.idol.board.controller.comment.command;

import com.idol.board.dto.request.comment.CommentCreateRequestDto;
import com.idol.board.dto.request.comment.CommentUpdateRequestDto;
import com.idol.board.usecase.comment.command.CreateCommentUseCase;
import com.idol.board.usecase.comment.command.DeleteCommentUseCase;
import com.idol.board.usecase.comment.command.UpdateCommentUseCase;
import com.idol.global.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor
public class CommentController {
    private final CreateCommentUseCase createCommentUseCase;
    private final DeleteCommentUseCase deleteCommentUseCase;
    private final UpdateCommentUseCase updateCommentUseCase;

    @PostMapping("/{articleId}/comment")
    public ApiResponse<Long>  createComment(@PathVariable Long articleId, @RequestBody CommentCreateRequestDto requestDto) {
        // TODO :: 인증 연결
        Long userId = 1231414314L;
        Long commentId = createCommentUseCase.createComment(requestDto, userId, articleId);
        
        return ApiResponse.ok(commentId, "댓글 생성 성공");
    }

    @DeleteMapping("/{articleId}/comment/{commentId}")
    public ApiResponse<Long> deleteComment(@PathVariable(name = "articleId") Long articleId, @PathVariable(name = "commentId") Long commentId) {
        deleteCommentUseCase.delete(commentId);
        return ApiResponse.ok(commentId, "댓글 삭제 성공");
    }

    @PatchMapping("/{articleId}/comment")
    public ApiResponse<Long>  updateComment(@PathVariable Long articleId, @RequestBody CommentUpdateRequestDto requestDto) {
        // TODO :: 인증 연결
        Long userId = 1231414314L;
        Long commentId = updateCommentUseCase.updateComment(requestDto);

        return ApiResponse.ok(commentId, "댓글 생성 성공");
    }
}
