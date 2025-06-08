package com.idol.global.exception.handler;

import com.idol.global.exception.ArticleNotFoundException;
import com.idol.global.exception.AuthenticationException;
import com.idol.global.exception.BadRequestException;
import com.idol.global.exception.NotFoundException;
import com.idol.global.exception.CommentNotFoundException;
import com.idol.global.exception.ConflictException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    //예시 코드
    @ExceptionHandler(BadRequestException.class)
    ProblemDetail handleBadRequestException(final BadRequestException e) {

        //status와 에러에 대한 자세한 설명
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());

        // 아래와 같이 필드 확장 가능
        problemDetail.setTitle("잘못된 요청입니다");

        return problemDetail;
    }

    @ExceptionHandler(ConflictException.class)
    ProblemDetail handleConflictException(final ConflictException e) {

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, e.getMessage());

        problemDetail.setTitle("데이터 충돌 에러");

        return problemDetail;
    }

    @ExceptionHandler(AuthenticationException.class)
    ProblemDetail handleAuthenticationException(final AuthenticationException e) {

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, e.getMessage());

        problemDetail.setTitle("인증 실패");

        return problemDetail;
    }

    @ExceptionHandler(NotFoundException.class)
    ProblemDetail handleNotFoundException(final NotFoundException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());

        // 리소스 타입에 따라 다른 제목 설정
        String title = switch (e.getResourceName()) {
            case "Article" -> "게시물을 찾을 수 없습니다";
            case "Comment" -> "댓글을 찾을 수 없습니다";
            default -> "리소스를 찾을 수 없습니다";
        };

        problemDetail.setTitle(title);
        return problemDetail;
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    ProblemDetail handleMethodArgumentNotValid(final MethodArgumentNotValidException e) {

        String errorMessage = e.getBindingResult().getAllErrors().getFirst().getDefaultMessage();
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, errorMessage);

        problemDetail.setTitle("유효성 검증 실패");

        return problemDetail;
    }

}
