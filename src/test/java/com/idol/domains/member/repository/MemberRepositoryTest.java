package com.idol.domains.member.repository;

import com.idol.domains.member.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest
@Import(MemberRepositoryImpl.class)
class MemberRepositoryTest {

    @Autowired
    private MemberRepositoryImpl memberRepositoryImpl;

    @Autowired
    private MemberJpaRepository memberJpaRepository;

    @DisplayName("Member 엔티티를 저장할 수 있다.")
    @Test
    void saveMember() {
        // given
        Member member = Member.builder()
                .email("test@example.com")
                .password("password123!")
                .nickname("테스트유저")
                .build();

        // when
        Member savedMember = memberRepositoryImpl.save(member);

        // then
        assertThat(savedMember.getMemberId()).isNotNull();
        assertThat(savedMember.getEmail()).isEqualTo("test@example.com");
        assertThat(savedMember.getPassword()).isEqualTo("password123!");
        assertThat(savedMember.getNickname()).isEqualTo("테스트유저");
        assertThat(savedMember.getProfileImgUrl()).isEmpty();
    }

    @DisplayName("저장된 User는 DB에서 조회할 수 있다.")
    @Test
    void savedMemberCanBeFound() {
        // given
        Member member = Member.builder()
                .email("test@example.com")
                .password("password123!")
                .nickname("테스트유저")
                .build();
        Member savedMember = memberRepositoryImpl.save(member);

        // when
        Member foundMember = memberJpaRepository.findById(savedMember.getMemberId())
                .orElseThrow();

        // then
        assertThat(foundMember.getMemberId()).isEqualTo(savedMember.getMemberId());
        assertThat(foundMember.getEmail()).isEqualTo("test@example.com");
    }

    @DisplayName("이메일로 회원 존재 여부를 확인할 수 있다.")
    @Test
    void existsByMemberEmail() {
        // given
        Member member = Member.builder()
                .email("test@example.com")
                .password("password123!")
                .nickname("테스트유저")
                .build();
        memberRepositoryImpl.save(member);

        // when & then
        assertThat(memberRepositoryImpl.existsByMemberEmail("test@example.com")).isTrue();
        assertThat(memberRepositoryImpl.existsByMemberEmail("notexist@example.com")).isFalse();
    }

    @DisplayName("닉네임으로 회원 존재 여부를 확인할 수 있다.")
    @Test
    void existsByNickname() {
        // given
        Member member = Member.builder()
                .email("test@example.com")
                .password("password123!")
                .nickname("테스트유저")
                .build();
        memberRepositoryImpl.save(member);

        // when & then
        assertThat(memberRepositoryImpl.existsByNickname("테스트유저")).isTrue();
        assertThat(memberRepositoryImpl.existsByNickname("존재하지않는닉네임")).isFalse();
    }

    @DisplayName("여러 회원이 있을 때 특정 이메일 존재 여부를 정확히 확인할 수 있다.")
    @Test
    void existsByMemberEmailWithMultipleMembers() {
        // given
        Member member1 = Member.builder()
                .email("test1@example.com")
                .password("password123!")
                .nickname("테스트유저1")
                .build();

        Member member2 = Member.builder()
                .email("test2@example.com")
                .password("password123!")
                .nickname("테스트유저2")
                .build();

        memberRepositoryImpl.save(member1);
        memberRepositoryImpl.save(member2);

        // when & then
        assertThat(memberRepositoryImpl.existsByMemberEmail("test1@example.com")).isTrue();
        assertThat(memberRepositoryImpl.existsByMemberEmail("test2@example.com")).isTrue();
        assertThat(memberRepositoryImpl.existsByMemberEmail("test3@example.com")).isFalse();
    }

    @DisplayName("여러 회원이 있을 때 특정 닉네임 존재 여부를 정확히 확인할 수 있다.")
    @Test
    void existsByNicknameWithMultipleMembers() {
        // given
        Member member1 = Member.builder()
                .email("test1@example.com")
                .password("password123!")
                .nickname("테스트유저1")
                .build();

        Member member2 = Member.builder()
                .email("test2@example.com")
                .password("password123!")
                .nickname("테스트유저2")
                .build();

        memberRepositoryImpl.save(member1);
        memberRepositoryImpl.save(member2);

        // when & then
        assertThat(memberRepositoryImpl.existsByNickname("테스트유저1")).isTrue();
        assertThat(memberRepositoryImpl.existsByNickname("테스트유저2")).isTrue();
        assertThat(memberRepositoryImpl.existsByNickname("테스트유저3")).isFalse();
    }
}
