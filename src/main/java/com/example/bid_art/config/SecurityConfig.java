package com.example.bid_art.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // 1. H2 콘솔에 대한 모든 보안 검사 해제
            .authorizeHttpRequests(auth -> auth
                // 정적 리소스(CSS, JS) 및 H2 콘솔 경로를 화이트리스트에 등록
                .requestMatchers(PathRequest.toH2Console()).permitAll()
                .anyRequest().authenticated()
            )
            // 2. CSRF 비활성화 (H2 콘솔 접속 시 필수)
            .csrf(csrf -> csrf.disable())
            // 3. H2 콘솔 프레임 노출 허용
            .headers(headers -> headers
                .frameOptions(frame -> frame.sameOrigin())
            )
            // 4. 나머지 페이지를 위한 기본 로그인 창
            .formLogin(form -> form.defaultSuccessUrl("/", true));

        return http.build();
    }
}