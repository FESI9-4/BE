package com.idol.global.common.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ApiResponse<T> {

    public static final String EMPTY = "";

    private int statusCode;
    private String message;
    private T data;

    public static <T> ApiResponse<T> ok(T data, String message) {
        return new ApiResponse<>(200, message, data);
    }

    public static <T> ApiResponse<T> ok(int statusCode, T data, String message) {
        return new ApiResponse<>(statusCode, message, data);
    }

    public static ApiResponse<String> okWithoutData(int statusCode, String message) {
        return new ApiResponse<>(statusCode, message, EMPTY);
    }

    public ApiResponse(int statusCode, String message, T data) {
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
    }
}
