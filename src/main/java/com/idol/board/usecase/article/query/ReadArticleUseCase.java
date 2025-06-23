package com.idol.board.usecase.article.query;

import com.idol.board.domain.BigCategory;
import com.idol.board.domain.SmallCategory;
import com.idol.board.domain.entity.Article;
import com.idol.board.dto.response.article.ArticleListImgResponseDto;
import com.idol.board.dto.response.article.ArticleListResponseDto;
import com.idol.board.dto.response.article.ArticleReadResponseDto;
import com.idol.board.dto.response.participant.ParticipantResponseDto;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Timestamp;
import java.util.List;

public interface ReadArticleUseCase {
    ArticleReadResponseDto readArticle(Long articleId, Long userId);

    List<ArticleListImgResponseDto> searchArticleList(BigCategory bigCategory, SmallCategory smallCategory, String location, Long date, String sort, boolean sortAsc, Long limit, Long page, Long memberId);
}