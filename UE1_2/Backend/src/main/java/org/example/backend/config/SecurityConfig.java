package org.example.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;

@EnableMethodSecurity
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(request -> {
                    var config = new org.springframework.web.cors.CorsConfiguration();
                    config.setAllowedOrigins(List.of("http://localhost:5173"));
                    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    config.setAllowedHeaders(List.of(
                            "Authorization",
                            "Content-Type",
                            "If-Match",
                            "If-None-Match",
                            "X-Requested-With"
                    ));
                    config.setExposedHeaders(List.of(
                            "ETag"
                    ));
                    config.setAllowCredentials(true);
                    return config;
                })).authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/users/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/**").authenticated()
                        .anyRequest().permitAll()
                )
                .headers(headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
                )
                .formLogin(form -> form
                        .loginProcessingUrl("/api/users/login")
                        .usernameParameter("username")           // Explizit den Parameter setzen
                        .passwordParameter("password")
                        .successHandler((req, res, auth) -> {
                            res.setStatus(200);
                            res.setContentType("application/json");

                            var user = (org.springframework.security.core.userdetails.User) auth.getPrincipal();

                            String json = """
                                    {
                                        "username": "%s",
                                        "roles": "%s"
                                    }
                                    """.formatted(
                                    user.getUsername(),
                                    user.getAuthorities().toString()
                            );

                            res.getWriter().write(json);
                        }).failureHandler((req, res, ex) -> res.setStatus(401))
                )
                .logout(logout -> logout
                        .logoutUrl("/api/users/logout")
                        .logoutSuccessHandler((req, res, auth) -> {
                            res.setStatus(200);
                            res.getWriter().write("{\"success\": true}");
                            res.setContentType("application/json");
                        })
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}