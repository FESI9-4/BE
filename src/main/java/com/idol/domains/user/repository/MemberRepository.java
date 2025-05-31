package com.idol.domains.user.repository;

import com.idol.domains.user.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class MemberRepository {

    private final MemberJpaRepository memberJpaRepository;

    public Member save(Member member) {
        return memberJpaRepository.save(member);
    }

    public boolean existsByMemberEmail(String email) {
        return memberJpaRepository.existsByEmail(email);
    }

    public boolean existsByNickname(String nickname) {
        return memberJpaRepository.existsByNickname(nickname);
    }
}
