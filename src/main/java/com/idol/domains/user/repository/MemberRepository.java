package com.idol.domains.user.repository;

import com.idol.domains.user.domain.Member;

public interface MemberRepository {

    Member save(Member member);

    boolean existsByMemberEmail(String email);

    boolean existsByNickname(String nickname);
}
