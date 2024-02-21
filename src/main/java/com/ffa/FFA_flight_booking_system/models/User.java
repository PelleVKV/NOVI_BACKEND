package com.ffa.FFA_flight_booking_system.models;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "`user`") // TODO: change to `users` and test application
public class User {
    @Id
    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @OneToMany(
            targetEntity =  Authority.class,
            mappedBy = "username",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    private Set<Authority> authorities = new HashSet<>();

    @OneToMany(
            targetEntity =  Reservation.class,
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private Set<Reservation> reservations = new HashSet<>();

    @Column(name = "enabled", nullable = false)
    private boolean enabled = true;

    public void addAuthority(Authority authority) {
        this.authorities.add(authority);
    }

    public Set<Authority> getAuthorities() {
        return authorities;
    }

    public boolean hasAuthority(String authority) {
        return authorities.stream().anyMatch(a -> a.getAuthority().equals(authority));
    }

    public void removeAuthority(Authority authority) {
        this.authorities.remove(authority);
    }

    public void addReservation(Reservation reservation) {
        this.reservations.add(reservation);
    }

    public Set<Reservation> getReservations() {
        return reservations;
    }

    public void removeReservation(Reservation reservation) {
        this.reservations.remove(reservation);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public User(String username) {
        this.username = username;
    }

    public User() {}

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
