package com.ffa.FFA_flight_booking_system.repositories;

import com.ffa.FFA_flight_booking_system.models.Airport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AirportRepository extends JpaRepository<Airport, String> {
    Optional<Airport> findByAirportName(String airportName);
}
