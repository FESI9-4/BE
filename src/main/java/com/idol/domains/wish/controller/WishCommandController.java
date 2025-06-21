package com.idol.domains.wish.controller;

import com.idol.domains.auth.util.annotation.MemberId;
import com.idol.domains.wish.dto.request.WishLikeRequestDto;
import com.idol.domains.wish.usecase.WishUnlikeUsecase;
import com.idol.domains.wish.usecase.WishlikeUsecase;
import com.idol.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "찜 API", description = "찜 관련 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class WishCommandController {

    private final WishlikeUsecase wishlikeUsecase;
    private final WishUnlikeUsecase wishUnlikeUsecase;

    @Operation(summary = "찜 등록", description = "게시물을 찜 목록에 추가합니다.")
    @PostMapping("/wishlike")
    public ApiResponse<Void> wishLike(
            @Valid @RequestBody WishLikeRequestDto requestDto,
            @MemberId Long memberId
    ) {
        wishlikeUsecase.WishLike(requestDto, memberId);
        return ApiResponse.ok(201, null, "찜 등록 성공");
    }

    @Operation(summary = "찜 삭제", description = "게시물을 찜 목록에서 삭제합니다.")
    @DeleteMapping("/wishlike/{articleId}")
    public ApiResponse<Void> wishUnlike(
            @Parameter(description = "게시물 ID", required = true)
            @PathVariable Long articleId,
            @MemberId Long memberId
    ) {
        wishUnlikeUsecase.WishUnlike(articleId, memberId);
        return ApiResponse.ok(200, null, "찜 삭제 성공");
    }
}