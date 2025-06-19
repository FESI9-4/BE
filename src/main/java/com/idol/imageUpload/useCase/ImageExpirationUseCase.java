package com.idol.imageUpload.useCase;

import java.time.Duration;
import java.util.Date;

public interface ImageExpirationUseCase {
    Duration getExpirationDuration();  // Date 대신 Duration 반환
}
