package com.idol.global.exception;

public class ArticleNotFoundException extends RuntimeException {
    public ArticleNotFoundException(String message) {
        super(message);
    }

    public ArticleNotFoundException(Long articleId) {
        super("게시물을 찾을 수 없습니다: " + articleId);
    }
}
