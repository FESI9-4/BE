package com.idol.global.exception;

import lombok.Getter;

@Getter
public class ForbiddenException extends RuntimeException {
    private final String resourceName;  // 리소스 이름 ("Article", "Comment")
    private final Object fieldValue;    // 필드 값

    public ForbiddenException(String resourceName, Object fieldValue) {
        super(String.format("%s 인원이 이미 찼습니다 : '%s'", resourceName,  fieldValue));
        this.resourceName = resourceName;
        this.fieldValue = fieldValue;
    }
}