package com.idol.domains.wish.controller;

import com.idol.domains.auth.util.annotation.MemberId;
import com.idol.domains.wish.dto.request.WishLikeRequestDto;
import com.idol.domains.wish.usecase.GetWishListByMemberIdUsecase;
import com.idol.domains.wish.usecase.WishUnlikeUsecase;
import com.idol.domains.wish.usecase.WishlikeUsecase;
import com.idol.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "찜 Query API", description = "찜 조회 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class WishQueryController {

    private final GetWishListByMemberIdUsecase getWishListByMemberIdUsecase;

    @Operation(summary = "찜 목록 조회", description = "회원의 찜한 게시물 ID 목록을 조회합니다.")
    @GetMapping("/wishlist")
    public ApiResponse<List<Long>> getWishList(
            @MemberId Long memberId
    ) {
        List<Long> articleIds = getWishListByMemberIdUsecase.findArticleIdsByMemberId(memberId);
        return ApiResponse.ok(200, articleIds, "찜 목록 조회 성공");
    }
}
