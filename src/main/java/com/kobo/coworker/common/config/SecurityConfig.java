package com.kobo.coworker.common.config;

import com.kobo.coworker.common.security.JwtRequestFilter;
import com.kobo.coworker.common.security.TokenProvider;
import com.kobo.coworker.user.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;
    private final TokenProvider tokenProvider;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService, @Lazy TokenProvider tokenProvider) {
        this.customUserDetailsService = customUserDetailsService;
        this.tokenProvider = tokenProvider;
    }

    @Bean
    public JwtRequestFilter jwtRequestFilter() {
        return new JwtRequestFilter(customUserDetailsService, tokenProvider);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CsrfTokenRepository csrfTokenRepository() {
        return CookieCsrfTokenRepository.withHttpOnlyFalse();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(
                                new AntPathRequestMatcher("/swagger-ui/**"),
                                new AntPathRequestMatcher("/v3/api-docs/**"),
                                new AntPathRequestMatcher("/api/auth/login"),
                                new AntPathRequestMatcher("/api/user/signup"),
                                new AntPathRequestMatcher("/api/findPassword/**")
                        ).permitAll()
                        .requestMatchers(
                                new AntPathRequestMatcher("/api/auth/logout"),
                                new AntPathRequestMatcher("/api/auth/delete")
                        ).authenticated()
                        .anyRequest().authenticated()
                )
                .logout(logout -> logout
                        .logoutUrl("/api/auth/logout")
                        .logoutSuccessHandler((request, response, authentication) -> {
                            response.setStatus(HttpServletResponse.SC_OK);
                            response.getWriter().write("로그아웃 성공");
                            response.getWriter().flush();
                        })
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                )
                .addFilterBefore(jwtRequestFilter(), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOriginPatterns(List.of(
                "http://kobo-co.worker.s3-website-us-east-1.amazonaws.com",
                "https://kobo-co.worker.s3-website-us-east-1.amazonaws.com",
                "http://co-worker.store",
                "https://co-worker.store"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setExposedHeaders(List.of(
                "Authorization",
                "Content-Type",
                "Access-Control-Allow-Origin",
                "Access-Control-Allow-Credentials"
        ));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
