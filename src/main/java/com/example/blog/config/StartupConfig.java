package com.example.blog.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StartupConfig {
    @Bean
    public CommandLineRunner runAtStartup() {
        return args -> {
            System.out.println("Blog Service is starting");
        };
    }
}
