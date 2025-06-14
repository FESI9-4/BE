package com.idol.global.exception;

import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException {
    private final String resourceName;  // 리소스 이름 ("Article", "Comment")
    private final Object fieldValue;    // 필드 값

    public NotFoundException(String resourceName, Object fieldValue) {
        super(String.format("%s 내용을 찾을 수 없습니다 : '%s'", resourceName,  fieldValue));
        this.resourceName = resourceName;
        this.fieldValue = fieldValue;
    }
}