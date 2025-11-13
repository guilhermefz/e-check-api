package com.echeck.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig {

    /**
     * Define o bean para o PasswordEncoder (BCrypt é o padrão e seguro)
     * para que possa ser injetado em AuthService.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Nota: Para desativar a segurança padrão do Spring Boot para que seus
    // endpoints /auth fiquem abertos, você precisará de uma classe de segurança
    // mais completa que estenda WebSecurityConfigurerAdapter (versões antigas) ou
    // SecurityFilterChain (versões mais novas, 3.0+).
}