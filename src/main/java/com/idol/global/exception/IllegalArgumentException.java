package com.idol.global.exception;

import lombok.Getter;

@Getter
public class IllegalArgumentException extends RuntimeException {
    private final String resourceName;  // 리소스 이름 ("Article", "Comment")
    private final Object fieldValue;    // 필드 값

    public IllegalArgumentException(String resourceName, Object fieldValue) {
        super(String.format("%s 접근 권한이 없습니다 : '%s'", resourceName,  fieldValue));
        this.resourceName = resourceName;
        this.fieldValue = fieldValue;
    }
}