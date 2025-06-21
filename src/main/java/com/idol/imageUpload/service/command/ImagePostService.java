package com.idol.imageUpload.service.command;


import com.idol.imageUpload.dto.GetS3UrlDto;
import com.idol.imageUpload.useCase.ImageExpirationUseCase;
import com.idol.imageUpload.useCase.ImageUploadUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import java.net.URL;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImagePostService implements ImageUploadUseCase {

    private final S3Presigner s3Presigner;
    private final ImageExpirationUseCase  imageExpirationUseCase;

    // 버킷 이름
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    // S3에 이미지 업로드 주소 URL 요청
    @Override
    public GetS3UrlDto getPostS3Url(String fileName) {
        String key = "profile/" + UUID.randomUUID() + "/" + fileName;

        // PutObjectRequest: S3에 객체를 업로드하기 위한 요청
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();

        // PresignedPutObjectRequest: PUT 요청을 위한 presigned URL 생성 요청
        PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(builder -> builder
                .putObjectRequest(putObjectRequest)
                .signatureDuration(imageExpirationUseCase.getExpirationDuration())
                .build());

        // presignPutObject: PUT 요청을 위한 presigned URL 생성
        URL url = presignedRequest.url();

        return new GetS3UrlDto(url.toString(), key);
    }

}
