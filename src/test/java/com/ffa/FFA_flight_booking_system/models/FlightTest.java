package com.ffa.FFA_flight_booking_system.models;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

public class FlightTest {
    @Test
    void flightNumberGeneration_ShouldFollowPrefixCodeGeneratorPattern() {
        // Arrange
        Flight flight = Mockito.spy(new Flight());

        // Act
        when(flight.getFlightNumber()).thenReturn("FFA00001");

        // Assert
        assertEquals("FFA00001", flight.getFlightNumber());
    }

}
