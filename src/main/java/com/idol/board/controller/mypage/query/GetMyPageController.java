package com.idol.board.controller.mypage.query;

import com.idol.board.domain.BigCategory;
import com.idol.board.domain.SmallCategory;
import com.idol.board.dto.response.article.ArticleListResponseDto;
import com.idol.board.service.article.query.ReadArticleService;
import com.idol.board.usecase.article.query.ReadArticleUseCase;
import com.idol.domains.auth.util.annotation.MemberId;
import com.idol.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping("/api/myPage")
@RequiredArgsConstructor
@Tag(name = "마이페이지 Read API", description = "마이페이지 Read API")
public class GetMyPageController {
    private final ReadArticleUseCase readArticleUseCase;


    @Operation(summary = "나의 펜팔 리스트 출력", description = "나의 펜팔 리스트 출력")
     @GetMapping()
    public ApiResponse<List<ArticleListResponseDto>> readAllMyPage(
            @RequestParam(value = "limit")Long limit,
            @RequestParam(value = "page")Long page,
            @MemberId Long userId
    ){

        List<ArticleListResponseDto> dto = readArticleUseCase.searchMypageList(limit,page,userId);

        return  ApiResponse.ok(dto, "게시글 리스트 전체 조회 성공");
    }



}
