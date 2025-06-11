package com.idol.domains.member.usecase;

import com.idol.domains.member.domain.Member;

import java.util.Optional;

public interface FindMemberUseCase {
    Optional<Member> findByMemberEmail(String email);
}
