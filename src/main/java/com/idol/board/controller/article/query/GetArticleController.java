package com.idol.board.controller.article.query;

import com.idol.board.domain.BigCategory;
import com.idol.board.domain.SmallCategory;
import com.idol.board.dto.response.article.ArticleListImgResponseDto;
import com.idol.board.dto.response.article.ArticleListResponseDto;
import com.idol.board.dto.response.article.ArticleReadResponseDto;
import com.idol.board.dto.response.comment.CommentResponseDto;
import com.idol.board.usecase.article.query.ReadArticleUseCase;
import com.idol.board.usecase.comment.query.ReadCommentUseCase;
import com.idol.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor
@Tag(name = "게시글 Read API", description = "게시글 Read API")
public class GetArticleController {
    private final ReadArticleUseCase readArticleUseCase;

    @Operation(summary = "게시글 상세 페이지", description = "게시글 상세 페이지 출력")
    @GetMapping("/{articleId}")
    public ApiResponse<ArticleReadResponseDto> readArticle(@PathVariable Long articleId) {

        ArticleReadResponseDto articleReadResponseDto = readArticleUseCase.readArticle(articleId);

        return ApiResponse.ok(articleReadResponseDto, "상세 게시글 호출 성공");
    }


    @Operation(summary = "게시글 리스트 출력", description = "카테고리, 지역, 날짜, 정렬 기준으로 게시글 리스트 출력")
    // /bigCategory?=go&smallCategory?=lentBus&location?=”seoul”&date?=1747361368&sort?=recent&sortAsc=true&lastArticleId?=151381421513&limit?=10
    @GetMapping()
    public ApiResponse<List<ArticleListImgResponseDto>> readAllArticle(
            @RequestParam(value = "bigCategory", required=false)BigCategory bigCategory,
            @RequestParam(value = "smallCategory", required=false)SmallCategory smallCategory,
            @RequestParam(value = "location", required=false)String location,
            @RequestParam(value = "date", required=false) Long date,
            @RequestParam(value = "sort", required=false)String sort,
            @RequestParam(value = "sortAsc", required=false)boolean sortAsc,                // true : ASC, false : DESC
            @RequestParam(value = "limit")Long limit,
            @RequestParam(value = "page")Long page
            ){

        List<ArticleListImgResponseDto> dto = readArticleUseCase.searchArticleList(bigCategory, smallCategory, location, date, sort,  sortAsc,limit,  page);

        return  ApiResponse.ok(dto, "게시글 리스트 전체 조회 성공");
    }
}