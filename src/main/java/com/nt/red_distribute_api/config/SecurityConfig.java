package com.nt.red_distribute_api.config;

import com.nt.red_distribute_api.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Autowired
    private UserService userDetailService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        //configuration
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests.
                        antMatchers("/home").permitAll().
                        antMatchers("/",
                                        "/error",
                                        "/favicon.ico",
                                        "/**/*.png",
                                        "/**/*.gif",
                                        "/**/*.svg",
                                        "/**/*.jpg",
                                        "/**/*.html",
                                        "/**/*.css",
                                        "/**/*.js").permitAll().
                        antMatchers("/auth/login").permitAll().
                        anyRequest().authenticated())
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); // nothing to save on server
        
        return http.build();
    }
    @Bean
    public DaoAuthenticationProvider doDaoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        return daoAuthenticationProvider;
    }

}
