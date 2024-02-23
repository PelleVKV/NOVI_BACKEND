package com.ffa.FFA_flight_booking_system.repositories;

import com.ffa.FFA_flight_booking_system.models.Flight;
import com.ffa.FFA_flight_booking_system.models.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    @Override
    <S extends Reservation> S save(S entity);

    @Override
    void delete(Reservation entity);

    Reservation findByReservationNumber(String reservationNumber);

    @Transactional
    @Modifying
    @Query("DELETE FROM Reservation r WHERE r.flight = :flight")
    void deleteReservationsByFlight(@Param("flight") Flight flight);

    @Override
    void deleteAll(Iterable<? extends Reservation> entities);

    @Override
    void deleteAll();
}
