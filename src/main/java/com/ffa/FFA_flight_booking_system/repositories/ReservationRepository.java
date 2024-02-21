package com.ffa.FFA_flight_booking_system.repositories;

import com.ffa.FFA_flight_booking_system.models.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    @Override
    <S extends Reservation> S save(S entity);

    @Override
    void delete(Reservation entity);

    Reservation findByReservationNumber(String reservationNumber);

    @Override
    void deleteAll(Iterable<? extends Reservation> entities);

    @Override
    void deleteAll();
}
