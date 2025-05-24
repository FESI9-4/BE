package com.idol.domains.user.repository;

import com.idol.domains.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserJpaRepository extends JpaRepository<User, UUID> {

}
