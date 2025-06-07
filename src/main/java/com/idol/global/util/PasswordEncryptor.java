package com.idol.global.util;

public interface PasswordEncryptor {
    String encrypt(String password);
    boolean matches(String password, String encryptedPassword);
}
