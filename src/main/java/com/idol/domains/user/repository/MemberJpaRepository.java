package com.idol.domains.user.repository;

import com.idol.domains.user.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MemberJpaRepository extends JpaRepository<Member, UUID> {

    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
}
