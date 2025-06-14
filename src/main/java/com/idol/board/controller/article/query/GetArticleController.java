package com.idol.board.controller.article.query;

import com.idol.board.dto.response.article.ArticleReadResponseDto;
import com.idol.board.dto.response.comment.CommentResponseDto;
import com.idol.board.usecase.article.query.ReadArticleUseCase;
import com.idol.board.usecase.comment.query.ReadCommentUseCase;
import com.idol.global.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

}
