package com.ffa.FFA_flight_booking_system.repositories;

import com.ffa.FFA_flight_booking_system.dto.FlightDTO;
import com.ffa.FFA_flight_booking_system.models.Flight;
import com.ffa.FFA_flight_booking_system.models.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FlightRepository extends JpaRepository<Flight, String> {
    @Modifying
    @Query("DELETE FROM Flight f WHERE f.flightNumber = :flightNumber")
    void deleteWithQuery(@Param("flightNumber") String flightNumber);

    Flight findByFlightNumber(String flightNumber);
}
