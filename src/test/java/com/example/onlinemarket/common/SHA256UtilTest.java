package com.example.onlinemarket.common;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.example.onlinemarket.common.utils.SHA256Util;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SHA256UtilTest {

    private String originalString;

    @BeforeEach
    void setUp() {
        originalString = "testPassword";
    }

    @Test
    @DisplayName("SHA-256 암호화로 문자열을 정확하게 암호화한다")
    void testEncryptSHA256() {
        String encryptedString = SHA256Util.encryptSHA256(originalString);

        assertNotNull(encryptedString, "암호화된 문자열은 null이 아니어야 합니다.");
        assertFalse(encryptedString.isEmpty(), "암호화된 문자열은 비어있지 않아야 합니다.");
        assertEquals(64, encryptedString.length(), "SHA-256 암호화된 문자열의 길이는 64여야 합니다.");
    }

    @Test
    @DisplayName("동일한 입력값에 대해 SHA-256 암호화는 일관된 결과를 반환한다")
    void testEncryptSHA256Consistency() {
        String firstEncryptedString = SHA256Util.encryptSHA256(originalString);
        String secondEncryptedString = SHA256Util.encryptSHA256(originalString);

        assertEquals(firstEncryptedString, secondEncryptedString,
                "동일한 입력에 대해서는 일관된 암호화된 문자열이 생성되어야 합니다.");
    }

    @Test
    @DisplayName("다른 입력값에 대해 SHA-256 암호화는 다른 결과를 반환한다")
    void testEncryptSHA256WithDifferentInput() {
        String encryptedString = SHA256Util.encryptSHA256(originalString);
        String differentEncryptedString = SHA256Util.encryptSHA256("anotherPassword");

        assertNotEquals(encryptedString, differentEncryptedString,
                "다른 입력에 대해서는 다른 암호화된 문자열이 생성되어야 합니다.");
    }
}
