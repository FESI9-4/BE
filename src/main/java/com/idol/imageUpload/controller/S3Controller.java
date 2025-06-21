package com.idol.imageUpload.controller;

import com.idol.domains.auth.util.annotation.MemberId;
import com.idol.global.common.response.ApiResponse;
import com.idol.imageUpload.dto.GetS3UrlDto;
import com.idol.imageUpload.dto.PostS3UrlDto;
import com.idol.imageUpload.useCase.ImageGetUserCase;
import com.idol.imageUpload.useCase.ImageUploadUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/images")
@Tag(name = "S3 이미지 업로드 / 읽기 URL 요청 API", description = "S3 API")
public class S3Controller {
    private final ImageUploadUseCase imageUploadUseCase;
    private final ImageGetUserCase imageGetUserCase;

    @Operation(summary = "S3 업로드 URL 요청", description = "S3 이미지 파일 업로드할 URL 값 전송")
    @GetMapping( "/postImage")
    // TODO :: User값 매개변수 추가 예정
    public ApiResponse<GetS3UrlDto> getPostS3Url(String fileName) {
        GetS3UrlDto getS3UrlDto = imageUploadUseCase.getPostS3Url(fileName);
        return ApiResponse.ok(getS3UrlDto, "이미지 주소 저장 성공");
    }

    @Operation(summary = "S3 출력 URL 요청", description = "S3 이미지 파일 출력 URL 값 전송")
    @GetMapping( "/getImage")
    public ApiResponse<GetS3UrlDto> getGetS3Url(@RequestParam String key) {
        GetS3UrlDto getS3UrlDto = imageGetUserCase.getGetS3Url(key);

        return ApiResponse.ok(getS3UrlDto, "이미지 주소 전송 성공");
    }
}
