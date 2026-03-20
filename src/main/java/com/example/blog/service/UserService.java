package com.example.blog.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    public String hashPassword(String rawPassword) {
        return encoder.encode(rawPassword);
    }
}
