package com.idol.board.controller.mypage.command;

import com.idol.board.dto.request.myPage.MyPageUpdateRequestDto;
import com.idol.board.usecase.mypage.UpdateMyPageUseCase;
import com.idol.domains.auth.util.annotation.MemberId;
import com.idol.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/myPage")
@RequiredArgsConstructor
@Tag(name = "마이페이지 CUD API", description = "마이페이지 CUD API")
public class MyPageController {

    private final UpdateMyPageUseCase updateMyPageUseCase;


    @PatchMapping()
    public ApiResponse<Long> updateProfile(@RequestBody MyPageUpdateRequestDto dto, @MemberId Long userId){

        Long memberId = updateMyPageUseCase.updateProfile(dto,userId);
        return ApiResponse.ok(memberId,"개인 정보 수정 완료");
    }


}
