package com.idol.domains.user.repository;

import com.idol.domains.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest
@Import(UserRepository.class)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserJpaRepository userJpaRepository;

    @DisplayName("User 엔티티를 저장할 수 있다.")
    @Test
    void saveUser() {
        // given
        User user = User.builder()
                .email("test@example.com")
                .password("password123!")
                .nickname("테스트유저")
                .build();

        // when
        User savedUser = userRepository.save(user);

        // then
        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getEmail()).isEqualTo("test@example.com");
        assertThat(savedUser.getPassword()).isEqualTo("password123!");
        assertThat(savedUser.getNickname()).isEqualTo("테스트유저");
        assertThat(savedUser.getProfileImgUrl()).isEmpty();
    }

    @DisplayName("저장된 User는 DB에서 조회할 수 있다.")
    @Test
    void savedUserCanBeFound() {
        // given
        User user = User.builder()
                .email("test@example.com")
                .password("password123!")
                .nickname("테스트유저")
                .build();
        User savedUser = userRepository.save(user);

        // when
        User foundUser = userJpaRepository.findById(savedUser.getId())
                .orElseThrow();

        // then
        assertThat(foundUser.getId()).isEqualTo(savedUser.getId());
        assertThat(foundUser.getEmail()).isEqualTo("test@example.com");
    }

}