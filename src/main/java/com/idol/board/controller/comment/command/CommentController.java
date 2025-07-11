package com.idol.board.controller.comment.command;

import com.idol.board.dto.request.comment.CommentCreateRequestDto;
import com.idol.board.dto.request.comment.CommentUpdateRequestDto;
import com.idol.board.usecase.comment.command.CreateCommentUseCase;
import com.idol.board.usecase.comment.command.DeleteCommentUseCase;
import com.idol.board.usecase.comment.command.UpdateCommentUseCase;
import com.idol.domains.auth.util.annotation.MemberId;
import com.idol.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor
@Tag(name = "댓글 CUD API", description = "댓글 CUD API")
public class CommentController {
    private final CreateCommentUseCase createCommentUseCase;
    private final DeleteCommentUseCase deleteCommentUseCase;
    private final UpdateCommentUseCase updateCommentUseCase;

    @Operation(summary = "댓글 작성", description = "댓글 혹은 대댓글 작성합니다")
    @PostMapping("/{articleId}/comment")
    public ApiResponse<Long>  createComment(@PathVariable Long articleId, @RequestBody CommentCreateRequestDto requestDto, @MemberId Long writerId) {
        Long commentId = createCommentUseCase.createComment(requestDto, writerId, articleId);

        return ApiResponse.ok(commentId, "댓글 생성 성공");
    }

    @Operation(summary = "댓글 삭제", description = "댓글 Soft Delete")
    @DeleteMapping("/{articleId}/comment/{commentId}")
    public ApiResponse<Long> deleteComment(@PathVariable(name = "articleId") Long articleId, @PathVariable(name = "commentId") Long commentId, @MemberId Long writerId) {
        deleteCommentUseCase.delete(commentId,writerId);
        return ApiResponse.ok(commentId, "댓글 삭제 성공");
    }

    @Operation(summary = "댓글 수정", description = "댓글 수정")
    @PatchMapping("/{articleId}/comment")
    public ApiResponse<Long>  updateComment(@PathVariable Long articleId, @RequestBody CommentUpdateRequestDto requestDto, @MemberId Long writerId) {
        Long returnCommentId = updateCommentUseCase.updateComment(requestDto,writerId);

        return ApiResponse.ok(returnCommentId, "댓글 수정 성공");
    }
}
