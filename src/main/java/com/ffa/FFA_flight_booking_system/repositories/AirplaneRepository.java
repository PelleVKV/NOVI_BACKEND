package com.ffa.FFA_flight_booking_system.repositories;

import com.ffa.FFA_flight_booking_system.models.Airplane;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AirplaneRepository extends JpaRepository<Airplane, String> {
    Optional<Airplane> findByAirplaneCode(String airplaneNumber);
}
