package com.idol.imageUpload.service.command;

import com.idol.imageUpload.useCase.ImageExpirationUseCase;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ImageExpirationService implements ImageExpirationUseCase {
    @Override
    public Date getExpiration() {
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * 60 * 60; // 1시간으로 설정하기
        expiration.setTime(expTimeMillis);
        return expiration;
    }
}
