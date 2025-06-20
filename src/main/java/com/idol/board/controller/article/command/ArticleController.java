package com.idol.board.controller.article.command;

import com.idol.board.dto.request.article.ArticleCreateRequestDto;
import com.idol.board.dto.request.article.ArticleUpdateRequestDto;
import com.idol.board.usecase.article.command.CreateArticleUseCase;
import com.idol.board.usecase.article.command.DeleteArticleUseCase;
import com.idol.board.usecase.article.command.ParticipantUseCase;
import com.idol.board.usecase.article.command.UpdateArticleUseCase;
import com.idol.domains.auth.util.annotation.MemberId;
import com.idol.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor
@Tag(name = "게시글 CUD API", description = "게시글 CUD API")
public class ArticleController {
    private final CreateArticleUseCase createArticleUseCase;
    private final DeleteArticleUseCase deleteArticleUseCase;
    private final UpdateArticleUseCase updateArticleUserCase;
    private final ParticipantUseCase participantUseCase;

    @Operation(summary = "게시글 작성", description = "이미지, 주소, 게시글 내용 작성합니다.")
    @PostMapping()
    public ApiResponse<Long> createArticle(@RequestBody ArticleCreateRequestDto requestDto, @MemberId Long writerId) {
        Long articleId = createArticleUseCase.createArticle(requestDto, writerId);
        return ApiResponse.ok(articleId, "게시글 작성 성공");
    }


    @Operation(summary = "게시글 삭제", description = "게시글 Soft Delete")
    @DeleteMapping("/{articleId}")
    public ApiResponse<Long> deleteArticle(@PathVariable("articleId") Long articleId, @MemberId Long writerId) {
        Long resultId = deleteArticleUseCase.delete(articleId, writerId);

        return ApiResponse.ok(resultId, "게시물 삭제가 완료되었습니다.");
    }

    @Operation(summary = "게시글 수정", description = "게시글 수정")
    @PatchMapping("/{articleId}")
    public ApiResponse<Long> updateArticle(@RequestBody ArticleUpdateRequestDto requestDto, @PathVariable Long articleId, @MemberId Long writerId) {
        Long updatedArticleId = updateArticleUserCase.updateArticle(requestDto, writerId, articleId);
        return ApiResponse.ok(updatedArticleId, "게시글 수정 성공");
    }

    @Operation(summary = "게시글 모집 취소", description = "게시글 모집 취소")
    @PatchMapping("/{articleId}/statusClose")
    public ApiResponse<Long> updateArticleClose(@PathVariable Long articleId, @MemberId Long writerId) {
        Long updatedArticleId = updateArticleUserCase.updateOpenStatusClose(articleId, writerId);
        return ApiResponse.ok(updatedArticleId, "게시글 모집 취소");
    }


    @Operation(summary = "펜팔 참여", description = "펜팔 참여")
    @PostMapping("/{articleId}/fanFal")
    public ApiResponse<Long> joinFanFal(@PathVariable Long articleId, @MemberId Long writerId) {
        Long fanFalId = participantUseCase.joinFanFal(articleId, writerId);
        return ApiResponse.ok(fanFalId, "펜팔 참여 성공");
    }

    @Operation(summary = "펜팔 취소", description = "펜팔 취소")
    @DeleteMapping("/{articleId}/fanFal")
    public ApiResponse<Long> cancelFanFal(@PathVariable Long articleId, @MemberId Long writerId) {
        Long fanFalId = participantUseCase.cancelFanFal(articleId, writerId);
        return ApiResponse.ok(fanFalId, "펜팔 취소 성공");
    }


}
