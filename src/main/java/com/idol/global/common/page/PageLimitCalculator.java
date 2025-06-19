package com.idol.global.common.page;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

// 페이지마다 개수 구분 해주는 service
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PageLimitCalculator {

    /*
       page : 특정 페이지 요청
       pageSize : 한 페이지당 N개 게시글
       movablePageCount : 페이지네이션 UI에 N개 페이지 번호 표시 (10이면 총 10Page 출력)
     */
    public static Long calculatePageLimit(Long page, Long pageSize, Long movablePageCount){
        return (((page - 1) / movablePageCount) + 1) * pageSize * movablePageCount + 1;         // 페이징 처리 공식
    }
}
