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
    @Override
    <S extends Airport> S save(S entity);

    @Override
    List<Airport> findAll();

    Optional<Airport> findByAirportName(String airportName);

    @Override
    <S extends Airport> List<S> saveAll(Iterable<S> entities);

    @Override
    void deleteById(String s);

    @Override
    void delete(Airport entity);

    @Override
    void deleteAllById(Iterable<? extends String> strings);

    @Override
    void deleteAll(Iterable<? extends Airport> entities);

    @Override
    void deleteAll();
}
