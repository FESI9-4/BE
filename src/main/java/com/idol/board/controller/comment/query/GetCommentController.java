package com.idol.board.controller.comment.query;

import com.idol.board.dto.request.comment.CommentCreateRequestDto;
import com.idol.board.dto.response.comment.CommentResponseDto;
import com.idol.board.usecase.comment.query.ReadCommentUseCase;
import com.idol.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor
@Tag(name = "댓글 Read API", description = "댓글 Read API")
public class GetCommentController {
    private final ReadCommentUseCase readCommentUseCase;

    @Operation(summary = "댓글 리스트 출력", description = "댓글 리스트  무한 스크롤 출력")
    @GetMapping("/{articleId}/comment")
    public ApiResponse<List<CommentResponseDto>> readComment(
            @PathVariable Long articleId,
            @RequestParam(value = "lastParentCommentId", required = false) Long lastParentCommentId,
            @RequestParam(value = "lastCommentId", required = false) Long lastCommentId,
            @RequestParam(value = "pageSize") Long pageSize){

        List<CommentResponseDto> responseDto = readCommentUseCase.readAll(articleId, lastParentCommentId, lastCommentId, pageSize);

        return ApiResponse.ok(responseDto, "댓글 리스트 호출 성공");
    }

}
