package com.idol.domains.member.service.query;

import com.idol.domains.member.domain.Member;
import com.idol.domains.member.repository.MemberRepository;
import com.idol.domains.member.usecase.FindMemberUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class FindMemberService implements FindMemberUseCase {

    private final MemberRepository memberRepository;

    @Override
    public Optional<Member> findByMemberEmail(String email) {
        return memberRepository.findByMemberEmail(email);
    }
}
