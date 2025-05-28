package com.idol.board.controller.article.command;

import com.idol.board.dto.request.article.ArticleCreateRequestDto;
import com.idol.board.usecase.article.command.CreateArticleUseCase;
import com.idol.global.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor
public class CreateArticleController {

    private final CreateArticleUseCase createArticleUseCase;

    @PostMapping()
    public ApiResponse<Long> createArticle(@RequestBody ArticleCreateRequestDto requestDto) {
        Long writerId = 1312441414L;
        Long articleId = createArticleUseCase.createArticle(requestDto, writerId);
        return ApiResponse.ok(articleId, "게시글 작성 성공");
    }


}
