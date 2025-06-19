package com.idol.board.controller.article.query;

import com.idol.board.domain.BigCategory;
import com.idol.board.domain.SmallCategory;
import com.idol.board.dto.response.article.ArticleListResponseDto;
import com.idol.board.dto.response.article.ArticleReadResponseDto;
import com.idol.board.dto.response.comment.CommentResponseDto;
import com.idol.board.usecase.article.query.ReadArticleUseCase;
import com.idol.board.usecase.comment.query.ReadCommentUseCase;
import com.idol.global.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor
public class GetArticleController {
    private final ReadArticleUseCase readArticleUseCase;

    @GetMapping("/{articleId}")
    public ApiResponse<ArticleReadResponseDto> readArticle(@PathVariable Long articleId) {

        ArticleReadResponseDto articleReadResponseDto = readArticleUseCase.readArticle(articleId);

        return ApiResponse.ok(articleReadResponseDto, "상세 게시글 호출 성공");
    }


    // /bigCategory?=go&smallCategory?=lentBus&location?=”seoul”&date?=1747361368&sort?=recent&sortAsc=true&lastArticleId?=151381421513&limit?=10
    @GetMapping()
    public ApiResponse<List<ArticleListResponseDto>> readAllArticle(
            @RequestParam(value = "bigCategory", required=false)BigCategory bigCategory,
            @RequestParam(value = "smallCategory", required=false)SmallCategory smallCategory,
            @RequestParam(value = "location", required=false)String location,
            @RequestParam(value = "date", required=false) Timestamp date,
            @RequestParam(value = "sort", required=false)String sort,
            @RequestParam(value = "sortAsc", required=false)boolean sortAsc,                // true : ASC, false : DESC
            @RequestParam(value = "limit")Long limit,
            @RequestParam(value = "page")Long page
            ){

        List<ArticleListResponseDto> dto = readArticleUseCase.searchArticleList(bigCategory, smallCategory, location, date, sort,  sortAsc,limit,  page);

        return  ApiResponse.ok(dto, "게시글 리스트 전체 조회 성공");
    }
}