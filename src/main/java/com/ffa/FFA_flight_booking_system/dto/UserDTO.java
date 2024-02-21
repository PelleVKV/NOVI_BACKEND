package com.ffa.FFA_flight_booking_system.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ffa.FFA_flight_booking_system.models.Authority;
import com.ffa.FFA_flight_booking_system.models.Reservation;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {
    @NotBlank
    public String username;

    @NotBlank
    @Size(min = 6, max = 30)
    public String password;

    public Set<Authority> authorities;

    public Set<Reservation> reservations;
    public boolean enabled = true;

    public UserDTO(String username, String password, boolean enabled) {
        this.username = username;
        this.password = password;
        this.enabled = enabled;
    }

    public UserDTO(String username, String password, Set<Authority> authorities, Set<Reservation> reservations) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.reservations = reservations;
    }

    public UserDTO() {}

    public boolean isEnabled() {
        return enabled;
    }

    public Set<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(Set<Reservation> reservations) {
        this.reservations = reservations;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }
}
