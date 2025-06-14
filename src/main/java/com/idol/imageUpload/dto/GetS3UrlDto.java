package com.idol.imageUpload.dto;

public record GetS3UrlDto (
        String preSignedUrl,
        String key
){
}
