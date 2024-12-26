package cn.example.springboot.springbootemployeemanagement;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoderTest {
    @Test
    public void encodePwd() {
        String password = "123";
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encryptedPassword = passwordEncoder.encode(password);
        passwordEncoder.matches(password, encryptedPassword);
        System.out.println(encryptedPassword);
    }
}