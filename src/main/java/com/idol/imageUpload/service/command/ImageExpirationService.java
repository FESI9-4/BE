package com.idol.imageUpload.service.command;

import com.idol.imageUpload.useCase.ImageExpirationUseCase;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Date;

@Service
public class ImageExpirationService implements ImageExpirationUseCase {
    @Override
    public Duration getExpirationDuration() {
        return Duration.ofHours(1);
    }
}
