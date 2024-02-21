package com.ffa.FFA_flight_booking_system.controllers;

import com.ffa.FFA_flight_booking_system.config.CustomUserDetailsService;
import com.ffa.FFA_flight_booking_system.utils.JwtUtil;
import com.ffa.FFA_flight_booking_system.dto.AuthenticationRequest;
import com.ffa.FFA_flight_booking_system.dto.AuthenticationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@CrossOrigin(origins = "https://localhost:5173")
@RequestMapping("/authority")
public class AuthenticationController {
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtUtil jwtUtil;


    @Autowired
    public AuthenticationController(AuthenticationManager authenticationManager, CustomUserDetailsService userDetailsService, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.customUserDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

    // GET MAPPING, RETRIEVING DATA

    @GetMapping(value = "/authenticated")
    public ResponseEntity<Object> getAuthenticatedUsers(Principal principal) {
        return ResponseEntity.ok().body(principal);
    }

    // POST MAPPING, SENDING DATA

    @PostMapping(value = "/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        String username = authenticationRequest.getUsername();
        String password = authenticationRequest.getPassword();

        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
            );
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }

        final UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
        final String jwt = jwtUtil.generateToken(userDetails);
        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }

}
