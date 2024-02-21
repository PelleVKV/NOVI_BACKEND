package com.ffa.FFA_flight_booking_system.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SpringSecurityConfig {
    private final CustomUserDetailsService customUserDetailsService;

    private final JwtRequestFilter jwtRequestFilter;

    public SpringSecurityConfig(CustomUserDetailsService customUserDetailsService, JwtRequestFilter jwtRequestFilter) {
        this.customUserDetailsService = customUserDetailsService;
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder pe) throws Exception {
        var auth = new DaoAuthenticationProvider();
        auth.setPasswordEncoder(pe);
        auth.setUserDetailsService(customUserDetailsService);
        return new ProviderManager(auth);
    }

    @Bean
    protected SecurityFilterChain filter(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .httpBasic(withDefaults())
                .cors().and()
                .authorizeHttpRequests((auth) -> auth
                        .antMatchers(HttpMethod.GET,"/user/**").permitAll()
                        .antMatchers(HttpMethod.GET,"/user").hasAuthority("ADMIN")
                        .antMatchers(HttpMethod.POST, "/user").hasAuthority("ADMIN")
                        .antMatchers(HttpMethod.POST,"/user/**").hasAuthority("ADMIN")

                        .antMatchers(HttpMethod.GET,"/airplanes").hasAuthority("ADMIN")
                        .antMatchers(HttpMethod.GET,"/airplanes/**").hasAuthority("ADMIN")
                        .antMatchers(HttpMethod.POST,"/airplanes").hasAuthority("ADMIN")
                        .antMatchers(HttpMethod.POST,"/airplanes/**").hasAuthority("ADMIN")

                        .antMatchers(HttpMethod.GET,"/airports").hasAuthority("ADMIN")
                        .antMatchers(HttpMethod.GET,"/airports/**").hasAuthority("ADMIN")
                        .antMatchers(HttpMethod.POST,"/airports").hasAuthority("ADMIN")
                        .antMatchers(HttpMethod.POST,"/airports/**").hasAuthority("ADMIN")

                        .antMatchers("/authenticated").authenticated()
                        .antMatchers("/authenticate").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
