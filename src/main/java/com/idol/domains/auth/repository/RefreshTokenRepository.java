package com.idol.domains.auth.repository;

import com.idol.domains.auth.domain.RefreshToken;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
    Optional<RefreshToken> findByMemberId(String memberId);
    Optional<RefreshToken> findByToken(String token);

    @Modifying
    void deleteByMemberId(String memberId);

    @Modifying
    void deleteByToken(String token);
}
