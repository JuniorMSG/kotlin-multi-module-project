package com.ms.multi.config

import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain


@Configuration
@EnableWebSecurity
class SecurityConfig {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .authorizeHttpRequests { auth ->
                auth
                    // 인증 없이 접근 가능한 경로
                    .requestMatchers(
                        "/api/cache/**",           // 캐시 테스트 API
                        "/api/debug/**",           // 디버그 API
                        "/actuator/health",        // Health check
                        "/error"                   // 에러 페이지
                    ).permitAll()

                    // 나머지는 인증 필요
                    .anyRequest().authenticated()
            }
            .formLogin { form ->
                form
                    .loginPage("/login")
                    .failureUrl("/login?error=invalid")
                    .permitAll()
            }
            .logout { logout ->
                logout.permitAll()
            }

        return http.build()
    }
}
