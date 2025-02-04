package com.example.jongnolback.configuration;

import com.example.jongnolback.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAutheticationFilter;

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .cors(httpSecurityCorsConfigurer -> {} )
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorizeRequests) -> {
                    authorizeRequests.requestMatchers("/").permitAll();
                    authorizeRequests.requestMatchers("/css/**").permitAll();
                    authorizeRequests.requestMatchers("/js/**").permitAll();
                    authorizeRequests.requestMatchers("/user/logout").permitAll();
                    authorizeRequests.requestMatchers("/user/login").permitAll();
                    authorizeRequests.requestMatchers("/user/join").permitAll();
                    authorizeRequests.requestMatchers("/user/id-check").permitAll();
                    authorizeRequests.requestMatchers("/user/updateprofile").permitAll();
                    authorizeRequests.requestMatchers("/quiz/newquiz").permitAll();
                    authorizeRequests.requestMatchers("/quiz/getquiz").permitAll();
                    authorizeRequests.requestMatchers("/quiz/getmyquiz").permitAll();
                    authorizeRequests.requestMatchers("/quiz/getcountqp").permitAll();
                    authorizeRequests.requestMatchers("/quiz/completequiz").permitAll();
                    authorizeRequests.requestMatchers("/quiz/solvequiz/{id}").permitAll();
                    authorizeRequests.requestMatchers("/user/nickname-check").permitAll();
                    authorizeRequests.anyRequest().authenticated();
                })
                .addFilterAfter(jwtAutheticationFilter, CorsFilter.class)
                .build();
    }
}