package com.idol.board.usecase.mypage.query;

import com.idol.board.dto.response.article.ArticleListResponseDto;
import com.idol.board.dto.response.mypage.MyPageQuestionResponseDto;
import com.idol.board.dto.response.mypage.UserAnswerResponseDto;
import com.idol.board.dto.response.mypage.UserAnswerTotalResponseDto;
import com.idol.board.dto.response.mypage.UserDataResponseDto;

import java.util.List;

public interface ReadMyPageUseCase {
    UserAnswerTotalResponseDto readMypageList(Long limit, Long page, Long userId);

    UserAnswerTotalResponseDto readJoinMypageList(Long limit, Long page, Long userId);

    UserDataResponseDto readUserInformation(Long userId);

    UserAnswerTotalResponseDto readAllAnswers(
            Long lastArticleId, Long userId, Long limit);
}
