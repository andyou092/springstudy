package com.example.study.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing//jpa에 대해서 감시를 실시 하겠다.
public class JpaConfig {
}
