package com.idol.imageUpload.useCase;

import com.idol.imageUpload.dto.GetS3UrlDto;

public interface ImageGetUserCase {
    GetS3UrlDto getGetS3Url(String key);
}
