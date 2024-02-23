package com.ffa.FFA_flight_booking_system.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserTest {

    @Test
    void addAuthority_ShouldAddAuthority() {
        // Arrange
        User user = new User();
        user.setUsername("john_doe");
        Authority authority = new Authority("john_doe", "USER");

        // Act
        user.addAuthority(authority);

        // Assert
        assertTrue(user.getAuthorities().contains(authority));
        assertEquals(user.getUsername(), authority.getUsername()); // Ensure bidirectional relationship
    }

    @Test
    void addReservation_ShouldAddReservation() {
        // Arrange
        User user = new User();
        user.setUsername("john_doe");
        Reservation reservation = new Reservation();

        // Act
        user.addReservation(reservation);

        // Assert
        assertTrue(user.getReservations().contains(reservation));
        assertEquals(user, reservation.getUser());
    }

}
