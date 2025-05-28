package com.idol.board.controller.article.command;

import com.idol.board.usecase.article.command.DeleteArticleUseCase;
import com.idol.global.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor
public class DeleteArticleController {
    private final DeleteArticleUseCase deleteArticleUseCase;

    @DeleteMapping("/{articleId}")
    public ApiResponse<Long> deleteArticle(@PathVariable("articleId") Long articleId) {
        Long resultId = deleteArticleUseCase.delete(articleId);

        return ApiResponse.ok(resultId, "게시물 삭제가 완료되었습니다.");
    }
}
