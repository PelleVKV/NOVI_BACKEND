package com.ffa.FFA_flight_booking_system.services;

import com.ffa.FFA_flight_booking_system.models.Airport;
import com.ffa.FFA_flight_booking_system.repositories.AirportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AirportService {
    private final AirportRepository airportRepository;

    @Autowired
    public AirportService(AirportRepository airportRepository) {
        this.airportRepository = airportRepository;
    }

    public List<Airport> findAll() {
        return airportRepository.findAll();
    }

    public Airport findByAirportName(String airportName) {
        return airportRepository.findByAirportName(airportName).orElse(null);
    }

    public Airport save(Airport airport) {
        return airportRepository.save(airport);
    }

    public List<Airport> saveAll(List<Airport> airports) {
        return airportRepository.saveAll(airports);
    }

}
