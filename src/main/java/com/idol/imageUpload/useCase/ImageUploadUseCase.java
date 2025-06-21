package com.idol.imageUpload.useCase;

import com.idol.imageUpload.dto.GetS3UrlDto;

import java.util.Date;

public interface ImageUploadUseCase {
    GetS3UrlDto getPostS3Url(String filename);
}
