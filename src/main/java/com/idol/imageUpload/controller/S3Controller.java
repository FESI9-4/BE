package com.idol.imageUpload.controller;

import com.idol.global.common.response.ApiResponse;
import com.idol.imageUpload.dto.GetS3UrlDto;
import com.idol.imageUpload.dto.PostS3UrlDto;
import com.idol.imageUpload.useCase.ImageGetUserCase;
import com.idol.imageUpload.useCase.ImageUploadUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/images")
public class S3Controller {
    private final ImageUploadUseCase imageUploadUseCase;
    private final ImageGetUserCase imageGetUserCase;

    // S3 이미지 파일 업로드할 URL 값 전송
    @GetMapping( "/postImage")
    // TODO :: User값 매개변수 추가 예정
    public ApiResponse<GetS3UrlDto> getPostS3Url(String fileName) {
        Long userId = 1321414L;
        GetS3UrlDto getS3UrlDto = imageUploadUseCase.getPostS3Url(userId, fileName);
        return ApiResponse.ok(getS3UrlDto, "이미지 주소 저장 성공");
    }



    @GetMapping( "/getImage")
    public ApiResponse<GetS3UrlDto> getGetS3Url(@RequestParam String key) {
        GetS3UrlDto getS3UrlDto = imageGetUserCase.getGetS3Url(key);

        return ApiResponse.ok(getS3UrlDto, "이미지 주소 전송 성공");
    }
}