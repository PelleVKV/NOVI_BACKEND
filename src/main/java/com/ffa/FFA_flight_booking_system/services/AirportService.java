package com.ffa.FFA_flight_booking_system.services;

import com.ffa.FFA_flight_booking_system.dto.AirportDTO;
import com.ffa.FFA_flight_booking_system.models.Airplane;
import com.ffa.FFA_flight_booking_system.models.Airport;
import com.ffa.FFA_flight_booking_system.repositories.AirportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

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

    public AirportDTO findByAirportName(String airportName) {
        Airport airport = airportRepository.findByAirportName(airportName);
        return fromAirport(airport);
    }

    public Airport save(Airport airport) {
        return airportRepository.save(airport);
    }

    public List<Airport> saveAll(List<Airport> airports) {
        return airportRepository.saveAll(airports);
    }

    public static AirportDTO fromAirport(Airport airport) {
        var dto = new AirportDTO();

        dto.setAirportName(airport.getAirportName());
        dto.setAirportCountry(airport.getAirportCountry());
        dto.setAirportCity(airport.getAirportCity());
        dto.setAirplanes(airport.getAirplanes());

        return dto;
    }

    public Airport toAirport(AirportDTO dto) {
        var airport = new Airport();

        airport.setAirportName(dto.getAirportName());
        airport.setAirportCountry(dto.getAirportCountry());
        airport.setAirportCity(dto.getAirportCity());
        airport.setAirplanes(dto.getAirplanes());

        return airport;
    }

}
