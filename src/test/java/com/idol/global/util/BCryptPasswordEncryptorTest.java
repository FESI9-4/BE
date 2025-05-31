package com.idol.global.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class BCryptPasswordEncryptorTest {

    private BCryptPasswordEncryptor passwordEncryptor;

    @BeforeEach
    void setUp() {
        passwordEncryptor = new BCryptPasswordEncryptor();
    }

    @DisplayName("비밀번호를 암호화하면 원본과 다른 문자열이 반환된다")
    @Test
    void encryptPassword() {
        // given
        String rawPassword = "password123!";

        // when
        String encryptedPassword = passwordEncryptor.encrypt(rawPassword);

        // then
        assertNotNull(encryptedPassword);
        assertNotEquals(rawPassword, encryptedPassword);
    }

    @DisplayName("같은 비밀번호를 여러 번 암호화해도 매번 다른 결과가 나온다")
    @Test
    void encryptSamePasswordMultipleTimes() {
        // given
        String rawPassword = "password123!";

        // when
        String encrypted1 = passwordEncryptor.encrypt(rawPassword);
        String encrypted2 = passwordEncryptor.encrypt(rawPassword);
        String encrypted3 = passwordEncryptor.encrypt(rawPassword);

        // then
        assertNotEquals(encrypted1, encrypted2);
        assertNotEquals(encrypted2, encrypted3);
        assertNotEquals(encrypted1, encrypted3);
    }

    @DisplayName("암호화된 비밀번호는 BCrypt 형식을 따른다")
    @Test
    void encryptedPasswordFollowsBCryptFormat() {
        // given
        String rawPassword = "password123!";

        // when
        String encryptedPassword = passwordEncryptor.encrypt(rawPassword);

        // then
        assertTrue(encryptedPassword.startsWith("$2a$"));
        assertEquals(60, encryptedPassword.length());
    }

    @DisplayName("원본 비밀번호와 암호화된 비밀번호가 일치하는지 확인할 수 있다")
    @Test
    void matchesCorrectPassword() {
        // given
        String rawPassword = "password123!";
        String encryptedPassword = passwordEncryptor.encrypt(rawPassword);

        // when
        boolean matches = passwordEncryptor.matches(rawPassword, encryptedPassword);

        // then
        assertTrue(matches);
    }

    @DisplayName("잘못된 비밀번호는 일치하지 않는다")
    @Test
    void doesNotMatchIncorrectPassword() {
        // given
        String rawPassword = "password123!";
        String wrongPassword = "wrongPassword123!";
        String encryptedPassword = passwordEncryptor.encrypt(rawPassword);

        // when
        boolean matches = passwordEncryptor.matches(wrongPassword, encryptedPassword);

        // then
        assertFalse(matches);
    }

    @ValueSource(strings = {"", " ", "a", "12345678", "!@#$%^&*()", "verylongpasswordwithmorethan50characters1234567890"})
    @DisplayName("다양한 형태의 비밀번호를 암호화할 수 있다")
    @ParameterizedTest
    void encryptVariousPasswords(String password) {
        // when
        String encryptedPassword = passwordEncryptor.encrypt(password);

        // then
        assertNotNull(encryptedPassword);
        assertTrue(passwordEncryptor.matches(password, encryptedPassword));
    }

    @DisplayName("빈 문자열도 암호화할 수 있다")
    @Test
    void encryptEmptyString() {
        // given
        String emptyPassword = "";

        // when
        String encryptedPassword = passwordEncryptor.encrypt(emptyPassword);

        // then
        assertNotNull(encryptedPassword);
        assertTrue(passwordEncryptor.matches(emptyPassword, encryptedPassword));
    }

    @NullSource
    @DisplayName("null 비밀번호 암호화 시 예외가 발생한다")
    @ParameterizedTest
    void encryptNullPasswordThrowsException(String nullPassword) {
        // when & then
        assertThrows(IllegalArgumentException.class, () -> {
            passwordEncryptor.encrypt(nullPassword);
        });
    }

    @DisplayName("null 비밀번호로 매칭 시도 시 예외가 발생한다")
    @Test
    void matchesWithNullPasswordThrowsException() {
        // given
        String encryptedPassword = passwordEncryptor.encrypt("password");

        // when & then
        assertThrows(IllegalArgumentException.class, () -> {
            passwordEncryptor.matches(null, encryptedPassword);
        });
    }

    @DisplayName("대소문자를 구분하여 암호화한다")
    @Test
    void encryptIsCaseSensitive() {
        // given
        String lowercase = "password";
        String uppercase = "PASSWORD";

        // when
        String encryptedLowercase = passwordEncryptor.encrypt(lowercase);
        String encryptedUppercase = passwordEncryptor.encrypt(uppercase);

        // then
        assertTrue(passwordEncryptor.matches(lowercase, encryptedLowercase));
        assertTrue(passwordEncryptor.matches(uppercase, encryptedUppercase));
        assertFalse(passwordEncryptor.matches(lowercase, encryptedUppercase));
        assertFalse(passwordEncryptor.matches(uppercase, encryptedLowercase));
    }

    @DisplayName("특수문자가 포함된 비밀번호도 정상적으로 처리한다")
    @Test
    void encryptPasswordWithSpecialCharacters() {
        // given
        String passwordWithSpecialChars = "p@ssw0rd!#$%^&*()_+-=[]{}|;':\",./<>?";

        // when
        String encryptedPassword = passwordEncryptor.encrypt(passwordWithSpecialChars);

        // then
        assertNotNull(encryptedPassword);
        assertTrue(passwordEncryptor.matches(passwordWithSpecialChars, encryptedPassword));
    }

    @DisplayName("유니코드 문자가 포함된 비밀번호도 정상적으로 처리한다")
    @Test
    void encryptPasswordWithUnicode() {
        // given
        String unicodePassword = "비밀번호123!한글도OK";

        // when
        String encryptedPassword = passwordEncryptor.encrypt(unicodePassword);

        // then
        assertNotNull(encryptedPassword);
        assertTrue(passwordEncryptor.matches(unicodePassword, encryptedPassword));
    }
}