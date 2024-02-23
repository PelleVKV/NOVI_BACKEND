package com.ffa.FFA_flight_booking_system.services;

import com.ffa.FFA_flight_booking_system.dto.AirportDTO;
import com.ffa.FFA_flight_booking_system.exceptions.NotFoundException;
import com.ffa.FFA_flight_booking_system.models.Airplane;
import com.ffa.FFA_flight_booking_system.models.Airport;
import com.ffa.FFA_flight_booking_system.repositories.AirportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class AirportService {
    private final AirportRepository airportRepository;

    @Autowired
    public AirportService(AirportRepository airportRepository) {
        this.airportRepository = airportRepository;
    }

    public List<AirportDTO> findAll() {
        List<Airport> airports = airportRepository.findAll();
        List<AirportDTO> airportDTOS = new ArrayList<>();
        for (Airport airport : airports) {
            airportDTOS.add(fromAirport(airport));
        }
        return airportDTOS;
    }

    public Airport findByAirportName(String airportName) {
        return airportRepository.findByAirportName(airportName).orElse(null);
    }

    public void updateAirport(String airportName, AirportDTO updatedAirportDTO) throws NotFoundException {
        Optional<Airport> existingAirportOptional = airportRepository.findByAirportName(airportName);

        if (existingAirportOptional.isPresent()) {
            Airport existingAirport = existingAirportOptional.get();

            existingAirport.setAirportName(updatedAirportDTO.getAirportName());
            existingAirport.setAirportCountry(updatedAirportDTO.getAirportCountry());
            existingAirport.setAirportCity(updatedAirportDTO.getAirportCity());

            airportRepository.save(existingAirport);
        } else {
            throw new NotFoundException("Airport not found");
        }
    }

    public AirportDTO save(AirportDTO airportDTO) {
        try {
            if (airportDTO == null) {
                throw new NotFoundException("Airport not found");
            }

            Airport airport = toAirport(airportDTO);
            airportRepository.save(airport);
            return airportDTO;
        } catch (Exception e) {
            throw new RuntimeException("Failed to save airport");
        }
    }

    public void deleteAirport(String airportName) {
        airportRepository.deleteById(airportName);
    }

    public void deleteAllAirports() {
        airportRepository.deleteAll();
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
