package com.idol.board.usecase.mypage.query;

import com.idol.board.dto.response.article.ArticleListResponseDto;
import com.idol.board.dto.response.mypage.MyPageQuestionResponseDto;
import com.idol.board.dto.response.mypage.UserDataResponseDto;

import java.util.List;

public interface ReadMyPageUseCase {
    List<ArticleListResponseDto> readMypageList(Long limit, Long page, Long userId);

    List<ArticleListResponseDto> readJoinMypageList(Long limit, Long page, Long userId);

    UserDataResponseDto readUserInformation(Long userId);

//    MyPageQuestionResponseDto readMyQuestion(Long userId, Long lastParentCommentId, Long lastCommentId, Long limit);
}
