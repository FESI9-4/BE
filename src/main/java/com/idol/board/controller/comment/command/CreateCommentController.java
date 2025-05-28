package com.idol.board.controller.comment.command;

import com.idol.board.dto.request.comment.CommentCreateRequestDto;
import com.idol.board.usecase.comment.command.CreateCommentUseCase;
import com.idol.global.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor
public class CreateCommentController {
    private final CreateCommentUseCase createCommentUseCase;

    @PostMapping("/{articleId}/comment")
    public ApiResponse<Long>  createComment(@PathVariable Long articleId, @RequestBody CommentCreateRequestDto requestDto) {
        // TODO :: 인증 연결
        Long userId = 1231414314L;
        Long commentId = createCommentUseCase.createComment(requestDto, userId, articleId);
        
        return ApiResponse.ok(commentId, "댓글 생성 성공");
    }
}
