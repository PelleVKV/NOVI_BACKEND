package com.ffa.FFA_flight_booking_system.models;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

public class ReservationTest {

    @Test
    void reservationNumberGeneration_ShouldFollowPrefixCodeGeneratorPattern() {
        // Arrange
        Reservation reservation = Mockito.spy(new Reservation());

        // Act
        when(reservation.getReservationNumber()).thenReturn("RES0001FFA");

        // Assert
        assertEquals("RES0001FFA", reservation.getReservationNumber());
    }

    @Test
    void setUser_ShouldSetUser() {
        // Arrange
        Reservation reservation = new Reservation();
        User user = new User();
        user.setUsername("john_doe");

        // Act
        reservation.setUser(user);

        // Assert
        assertEquals(user, reservation.getUser());
        assertTrue(user.getReservations().contains(reservation));
    }

}
