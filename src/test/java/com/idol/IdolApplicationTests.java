package com.idol;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
class IdolApplicationTests {

    @Test
    void contextLoads() {
        // 이 메서드는 Spring ApplicationContext가 정상적으로 로드되는지만 확인합니다.
        // 테스트 실패는 컨텍스트 로딩 중 문제가 있다는 것을 의미합니다.
    }

}
