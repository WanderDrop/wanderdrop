package com.wanderdrop.wserver.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {

    private static final String[] WHITE_LIST_URL = {"/api/auth/**"};
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(registry -> {
                    registry
                            .requestMatchers(WHITE_LIST_URL).permitAll()
                            .requestMatchers(HttpMethod.GET, "/api/attractions/**").permitAll()
                            .requestMatchers(HttpMethod.POST, "/api/attractions").hasAnyRole("ADMIN", "USER")
                            .requestMatchers(HttpMethod.PUT, "/api/attractions/**").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.GET, "/api/deletion-reasons/**").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.POST, "/api/deletion-reasons").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.GET, "/api/comments/**").permitAll()
                            .requestMatchers(HttpMethod.POST, "/api/comments").hasAnyRole("USER", "ADMIN")
                            .requestMatchers(HttpMethod.PUT, "/api/comments/**").hasRole("ADMIN")
                            .anyRequest().authenticated();
                })
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
