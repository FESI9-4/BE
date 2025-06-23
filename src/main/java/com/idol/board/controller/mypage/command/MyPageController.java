package com.idol.board.controller.mypage.command;

import com.idol.board.dto.request.myPage.MyPagePasswordUpdateRequestDto;
import com.idol.board.dto.request.myPage.MyPageUpdateRequestDto;
import com.idol.board.usecase.mypage.command.DeleteMyPageUseCase;
import com.idol.board.usecase.mypage.command.UpdateMyPageUseCase;
import com.idol.board.dto.response.mypage.UserDataResponseDto;
import com.idol.domains.auth.util.annotation.MemberId;
import com.idol.domains.member.dto.request.SignupMemberRequestDto;
import com.idol.domains.member.dto.response.SignupMemberResponseDto;
import com.idol.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/myPage")
@RequiredArgsConstructor
@Tag(name = "마이페이지 CUD API", description = "마이페이지 CUD API")
public class MyPageController {

    private final UpdateMyPageUseCase updateMyPageUseCase;
    private final DeleteMyPageUseCase deleteMyPageUseCase;

    @Operation(summary = "프로필 수정", description = "이미지, 닉네임, 자기소개 수정")
    @PatchMapping()
    public ApiResponse<Long> updateProfile(@RequestBody MyPageUpdateRequestDto dto, @MemberId Long userId){

        Long memberId = updateMyPageUseCase.updateProfile(dto,userId);
        return ApiResponse.ok(memberId,"개인 정보 수정 완료");
    }

    @Operation(summary = "비밀번호 변경", description = "비밀 번호 변경")
    @PutMapping()
    public ApiResponse<Long> updatePassword(@Valid @RequestBody MyPagePasswordUpdateRequestDto dto, @MemberId Long userId){
        Long memberId = updateMyPageUseCase.updatePassword(dto,userId);
        return ApiResponse.ok(memberId,"비밀 번호 변경 완료");
    }

    @Operation(summary = "내가 참여한 펜팔 삭제", description = "내가 참여하나 펜팔 삭제")
    @DeleteMapping("/{articleId}")
    public ApiResponse<Long> deleteMyFanFal(@PathVariable Long articleId, @MemberId Long userId){
        Long memberId = deleteMyPageUseCase.deleteMyFanFal(articleId, userId);
        return ApiResponse.ok(memberId,"내가 참여한 펜팔 삭제");
    }

}
