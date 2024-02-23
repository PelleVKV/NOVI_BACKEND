package com.ffa.FFA_flight_booking_system.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AirplaneTest {
    @Test
    void calculateAvailableSeats_ShouldReturnCorrectValue() {
        // Arrange
        Airplane airplane = new Airplane();
        airplane.setAirplaneCapacity(200);

        Flight flight1 = new Flight();
        flight1.setFilledSeats(50);

        airplane.getFlights().add(flight1);

        // Act
        int availableSeats = airplane.calculateAvailableSeats();

        // Assert
        assertEquals(150, availableSeats);
    }

    @Test
    void calculateAvailableSeats_WithNoFlights_ReturnsTotalCapacity() {
        // Arrange
        Airplane airplane = new Airplane();
        airplane.setAirplaneCapacity(100);

        // Act
        int availableSeats = airplane.calculateAvailableSeats();

        // Assert
        assertEquals(100, availableSeats);
    }
}
