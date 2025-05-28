package com.idol.imageUpload.service.command;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.idol.imageUpload.dto.GetS3UrlDto;
import com.idol.imageUpload.useCase.ImageExpirationUseCase;
import com.idol.imageUpload.useCase.ImageUploadUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URL;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImagePostService implements ImageUploadUseCase {
    private final AmazonS3 amazonS3Client;
    private final ImageExpirationUseCase  imageExpirationUseCase;

    // 버킷 이름
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Transactional(readOnly = true)
    public GetS3UrlDto getPostS3Url(Long memberId, String filename) {
        // filename 설정하기(profile 경로 + 멤버ID + 랜덤 값)
        String key = "profile/" + memberId + "/" + UUID.randomUUID() + "/" + filename;

        // url 유효기간 설정하기(1시간)
        Date expiration = imageExpirationUseCase.getExpiration();

        // presigned url 생성하기
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                getPostGeneratePresignedUrlRequest(key, expiration);

        URL url = amazonS3Client.generatePresignedUrl(generatePresignedUrlRequest);

        // return
        return new GetS3UrlDto(url.toExternalForm(), key);
    }

    /* post 용 URL 생성하는 메소드 */
    private GeneratePresignedUrlRequest getPostGeneratePresignedUrlRequest(String fileName, Date expiration) {
        GeneratePresignedUrlRequest generatePresignedUrlRequest
                = new GeneratePresignedUrlRequest(bucket, fileName)
                .withMethod(HttpMethod.PUT)
                .withKey(fileName)
                .withExpiration(expiration);
//        generatePresignedUrlRequest.addRequestParameter(
//                Headers.S3_CANNED_ACL,
//                CannedAccessControlList.PublicRead.toString());
        return generatePresignedUrlRequest;
    }
}
