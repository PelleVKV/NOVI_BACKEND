package com.ffa.FFA_flight_booking_system.models;
import com.ffa.FFA_flight_booking_system.models.Airport;
import com.ffa.FFA_flight_booking_system.models.Airplane;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AirportTest {
    @Test
    void removeAirplane_ShouldRemoveAirplaneFromAirport() {
        // Arrange
        Airport airport = new Airport();
        Airplane airplane = new Airplane();
        airport.getAirplanes().add(airplane);

        // Act
        airport.getAirplanes().remove(airplane);

        // Assert
        assertTrue(airport.getAirplanes().isEmpty());
        assertEquals(null, airplane.getResidingAirport());
    }
}
