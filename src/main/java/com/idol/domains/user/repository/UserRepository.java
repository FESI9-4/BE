package com.idol.domains.user.repository;

import com.idol.domains.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class UserRepository {

    private final UserJpaRepository userJpaRepository;

    public User save(User user) {
        return userJpaRepository.save(user);
    }
}
