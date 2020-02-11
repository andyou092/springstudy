package com.example.study.component;


import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class LoginUserAuditorAware implements AuditorAware<String> {//로그인 사용자를 감시하기 위해서 만든 클래스이다.
    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of("AdminServer");
    }

}
