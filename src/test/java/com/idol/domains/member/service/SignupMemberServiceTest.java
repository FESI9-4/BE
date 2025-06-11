package com.idol.domains.member.service;

import com.idol.domains.member.domain.Member;
import com.idol.domains.member.dto.request.SignupMemberRequestDto;
import com.idol.domains.member.dto.response.SignupMemberResponseDto;
import com.idol.domains.member.repository.MemberRepository;
import com.idol.domains.member.service.command.SignupMemberService;
import com.idol.global.util.PasswordEncryptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SignupMemberServiceTest {

    @InjectMocks
    private SignupMemberService signupMemberService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PasswordEncryptor passwordEncryptor;

    private SignupMemberRequestDto requestDto;
    private Member savedMember;

    @BeforeEach
    void setUp() throws Exception {
        requestDto = SignupMemberRequestDto.builder()
                .email("test@example.com")
                .password("password123!")
                .nickname("테스트유저")
                .build();

        savedMember = Member.builder()
                .email("test@example.com")
                .password("encodedPassword")
                .nickname("테스트유저")
                .build();

        setMemberId(savedMember, UUID.randomUUID());
        setCreatedAt(savedMember, LocalDateTime.now());
    }

    @DisplayName("회원가입 기능을 통해 Member를 생성할 수 있다.")
    @Test
    void signupSuccess() {
        // given
        given(memberRepository.existsByMemberEmail(anyString())).willReturn(false);
        given(memberRepository.existsByNickname(anyString())).willReturn(false);
        given(passwordEncryptor.encrypt(anyString())).willReturn("encodedPassword");
        given(memberRepository.save(any(Member.class))).willReturn(savedMember);

        // when
        SignupMemberResponseDto response = signupMemberService.signup(requestDto);

        // then
        assertThat(response).isNotNull();
        assertThat(response.memberId()).isNotNull();
        assertThat(response.email()).isEqualTo("test@example.com");
        assertThat(response.nickname()).isEqualTo("테스트유저");

        verify(memberRepository).existsByMemberEmail("test@example.com");
        verify(memberRepository).existsByNickname("테스트유저");
        verify(passwordEncryptor).encrypt("password123!");
        verify(memberRepository).save(any(Member.class));
    }

    private void setMemberId(Member member, UUID id) throws Exception {
        Field idField = Member.class.getDeclaredField("memberId");
        idField.setAccessible(true);
        idField.set(member, id);
    }

    private void setCreatedAt(Member member, LocalDateTime createdAt) throws Exception {
        Field field = Member.class.getSuperclass().getDeclaredField("createdAt");
        field.setAccessible(true);
        field.set(member, createdAt);
    }

}
