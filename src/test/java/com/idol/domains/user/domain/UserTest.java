package com.idol.domains.user.domain;

import com.idol.domains.user.dto.request.SignupUserRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    @DisplayName("SignupUserRequestDto의 데이터로 User 엔티티를 생성할 수 있다.")
    @Test
    void createUserFromSignupRequestDto() {
        // given
        SignupUserRequestDto requestDto = SignupUserRequestDto.builder()
                .email("test@example.com")
                .password("password123!")
                .nickname("테스트유저")
                .build();

        // when
        User user = User.from(requestDto);

        // then
        assertThat(user).isNotNull();
        assertThat(user.getEmail()).isEqualTo("test@example.com");
        assertThat(user.getPassword()).isEqualTo("password123!");
        assertThat(user.getNickname()).isEqualTo("테스트유저");
        assertThat(user.getProfileImgUrl()).isEmpty();
        assertThat(user.getId()).isNull();
    }

}
