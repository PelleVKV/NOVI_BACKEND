package com.ffa.FFA_flight_booking_system.config;

import com.ffa.FFA_flight_booking_system.dto.UserDTO;
import com.ffa.FFA_flight_booking_system.models.Authority;
import com.ffa.FFA_flight_booking_system.services.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;

    public CustomUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        UserDTO dto = userService.getUser(username);

        if (dto == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        String password = dto.getPassword();
        String encodedPassword = passwordEncoder.encode(password);

        Set<Authority> authorities = dto.getAuthorities();
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        for (Authority authority : authorities) {
            grantedAuthorities.add(new SimpleGrantedAuthority(authority.getAuthority()));
        }

        System.out.println("Found user: " + username + " with authority: " + grantedAuthorities + " and password: " + encodedPassword);
        return new org.springframework.security.core.userdetails.User(username, encodedPassword, grantedAuthorities);
    }
}
