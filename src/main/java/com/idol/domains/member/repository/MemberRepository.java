package com.idol.domains.member.repository;

import com.idol.domains.member.domain.Member;

import java.util.Optional;

public interface MemberRepository {

    Member save(Member member);

    boolean existsByMemberEmail(String email);

    boolean existsByNickname(String nickname);

    Optional<Member> findByMemberEmail(String email);
}
