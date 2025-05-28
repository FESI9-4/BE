package com.idol.imageUpload.service.query;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.idol.imageUpload.dto.GetS3UrlDto;

import com.idol.imageUpload.service.command.ImageExpirationService;
import com.idol.imageUpload.useCase.ImageExpirationUseCase;
import com.idol.imageUpload.useCase.ImageGetUserCase;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URL;
import java.util.Date;

@RequiredArgsConstructor
@Service
public class ImageGetService implements ImageGetUserCase {
    private final AmazonS3 amazonS3Client;
    private final ImageExpirationUseCase imageExpirationUseCase;

    // 버킷 이름
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Override
    @Transactional(readOnly = true)
    public GetS3UrlDto getGetS3Url(String key) {
        // url 유효기간 설정하기(1시간)
        Date expiration = imageExpirationUseCase.getExpiration();

        // presigned url 생성하기
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                getGetGeneratePresignedUrlRequest(key, expiration);

        URL url = amazonS3Client.generatePresignedUrl(generatePresignedUrlRequest);

        // return
        return new GetS3UrlDto(url.toExternalForm(), key);
    }

    /* get 용 URL 생성하는 메소드 */
    private GeneratePresignedUrlRequest getGetGeneratePresignedUrlRequest(String key, Date expiration) {
        return new GeneratePresignedUrlRequest(bucket, key)
                .withMethod(HttpMethod.GET)
                .withExpiration(expiration);
    }
}
