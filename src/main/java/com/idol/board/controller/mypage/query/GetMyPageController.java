package com.idol.board.controller.mypage.query;

import com.idol.board.domain.BigCategory;
import com.idol.board.domain.SmallCategory;
import com.idol.board.dto.response.article.ArticleListResponseDto;
import com.idol.board.dto.response.comment.CommentResponseDto;
import com.idol.board.dto.response.mypage.MyPageQuestionResponseDto;
import com.idol.board.dto.response.mypage.UserAnswerResponseDto;
import com.idol.board.dto.response.mypage.UserAnswerTotalResponseDto;
import com.idol.board.dto.response.mypage.UserDataResponseDto;
import com.idol.board.service.article.query.ReadArticleService;
import com.idol.board.usecase.article.query.ReadArticleUseCase;
import com.idol.board.usecase.mypage.query.ReadMyPageUseCase;
import com.idol.domains.auth.util.annotation.MemberId;
import com.idol.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping("/api/myPage")
@RequiredArgsConstructor
@Tag(name = "마이페이지 Read API", description = "마이페이지 Read API")
public class GetMyPageController {
    private final ReadMyPageUseCase readMypageUseCase;


    @Operation(summary = "나의 펜팔 출력", description = "나의 펜팔 출력")
    @GetMapping()
    public ApiResponse<UserAnswerTotalResponseDto> readAllJoinMyPage(
            @RequestParam(value = "limit")Long limit,
            @RequestParam(value = "page")Long page,
            @MemberId Long userId
    ){

        UserAnswerTotalResponseDto dto = readMypageUseCase.readJoinMypageList(limit,page,userId);

        return  ApiResponse.ok(dto, "나의 펜팔 리스트 조회 성공");
    }


    @Operation(summary = "내가 만든 펜팔 출력", description = "내가 만든 펜팔 출력")
    @GetMapping("/self")
    public ApiResponse<UserAnswerTotalResponseDto> readAllCreateMyPage(
            @RequestParam(value = "limit")Long limit,
            @RequestParam(value = "page")Long page,
            @MemberId Long userId
    ){

        UserAnswerTotalResponseDto dto = readMypageUseCase.readMypageList(limit,page,userId);

        return  ApiResponse.ok(dto, "나가 만든 펜팔 리스트 조회 성공");
    }

    @Operation(summary = "유저 정보 전달", description = "유저 정보 전달을 진행합니다.")
    @GetMapping("/user")
    public ApiResponse<UserDataResponseDto> readUserInformation(
            @MemberId Long userId
    ) {
        UserDataResponseDto response = readMypageUseCase.readUserInformation(userId);
        return ApiResponse.ok(response, "유저 정보 전달 성공");
    }


    @Operation(summary = "나의 답변 리스트 출력", description = "나의 답변 리스트 출력")
    @GetMapping("/answer")
    public ApiResponse<UserAnswerTotalResponseDto> readAllAnswers(
            @RequestParam(value = "lastArticleId", required = false) Long lastArticleId,
            @RequestParam(value = "limit")Long limit,
            @MemberId Long userId
    ) {
        UserAnswerTotalResponseDto response= readMypageUseCase.readAllAnswers(lastArticleId,userId, limit);
        return ApiResponse.ok(response, "나의 답변 리스트 출력 성공");
    }
}
