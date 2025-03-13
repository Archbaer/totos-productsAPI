package com.totos.productsAPI.config;

import com.totos.productsAPI.filter.JwtAuthenticationFilter;
import com.totos.productsAPI.services.PublicKeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private PublicKeyService publicKeyService;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF protection for stateless API
                .csrf(csrf -> csrf.disable())

                // Authorization rules
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/products/**").permitAll()
                )

                // Stateless session management
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // JWT authentication filter
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsManager() {
        UserDetails user = User.withDefaultPasswordEncoder()
                .username("user")
                .password("user123")
                .build();
            return new InMemoryUserDetailsManager(user);
    }

}
