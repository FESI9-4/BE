package com.idol.domains.member.domain;

import com.idol.domains.member.dto.request.SignupMemberRequestDto;
import com.idol.global.util.BCryptPasswordEncryptor;
import com.idol.global.util.PasswordEncryptor;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor
class MemberTest {

    private final PasswordEncryptor passwordEncryptor = new BCryptPasswordEncryptor();

    @DisplayName("SignupUserRequestDto의 데이터로 Member 엔티티를 생성할 수 있다.")
    @Test
    void createUserFromSignupRequestDto() {
        // given
        SignupMemberRequestDto requestDto = SignupMemberRequestDto.builder()
                .email("test@example.com")
                .password("password123!")
                .nickname("테스트유저")
                .build();

        String encryptedPassword = passwordEncryptor.encrypt(requestDto.password());

        // when
        Member member = Member.from(requestDto, encryptedPassword);

        // then
        assertThat(member).isNotNull();
        assertThat(member.getEmail()).isEqualTo("test@example.com");
        assertThat(member.getPassword()).isEqualTo(encryptedPassword);
        assertThat(member.getNickname()).isEqualTo("테스트유저");
        assertThat(member.getProfileImgUrl()).isEmpty();
        assertThat(member.getMemberId()).isNull();
    }

}
