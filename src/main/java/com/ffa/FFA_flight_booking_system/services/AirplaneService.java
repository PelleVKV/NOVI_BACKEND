package com.ffa.FFA_flight_booking_system.services;

import com.ffa.FFA_flight_booking_system.dto.AirplaneDTO;
import com.ffa.FFA_flight_booking_system.dto.AirportDTO;
import com.ffa.FFA_flight_booking_system.dto.FlightDTO;
import com.ffa.FFA_flight_booking_system.exceptions.NotFoundException;
import com.ffa.FFA_flight_booking_system.models.Airplane;
import com.ffa.FFA_flight_booking_system.models.Airport;
import com.ffa.FFA_flight_booking_system.models.Flight;
import com.ffa.FFA_flight_booking_system.repositories.AirplaneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class AirplaneService {
    private final AirplaneRepository airplaneRepository;
    private final AirportService airportService;

    @Autowired
    public AirplaneService(AirplaneRepository airplaneRepository, AirportService airportService) {
        this.airplaneRepository = airplaneRepository;
        this.airportService = airportService;
    }

    public List<AirplaneDTO> getAllAirplanes() {
        List<Airplane> airplanes = airplaneRepository.findAll();
        List<AirplaneDTO> airplaneDTOS = new ArrayList<>();
        for (Airplane airplane : airplanes) {
            airplaneDTOS.add(fromAirplane(airplane));
        }
        return airplaneDTOS;
    }

    public AirplaneDTO getAirplaneByAirplaneCode(String airplaneCode) throws NotFoundException {
        Airplane airplane = airplaneRepository.findByAirplaneCode(airplaneCode).orElse(null);

        if (airplane == null) {
            throw new NotFoundException("Airplane data invalid");
        }

        return fromAirplane(airplane);
    }

    public void deleteAllAirplanes() {
        airplaneRepository.deleteAll();
    }

    public void updateAirplane(String airplaneCode, AirplaneDTO updatedAirplaneDTO) throws NotFoundException {
        AirplaneDTO existingAirplaneDTO = getAirplaneByAirplaneCode(airplaneCode);

        if (existingAirplaneDTO != null) {

            existingAirplaneDTO.setAirplaneCode(updatedAirplaneDTO.getAirplaneCode());
            existingAirplaneDTO.setAirplaneCapacity(updatedAirplaneDTO.getAirplaneCapacity());
            existingAirplaneDTO.setResidingAirport(updatedAirplaneDTO.getResidingAirport());
            existingAirplaneDTO.setAirplaneType(updatedAirplaneDTO.getAirplaneType());

            saveAirplane(existingAirplaneDTO);
        } else {
            throw new NotFoundException("Airplane not found");
        }
    }

    public AirportDTO getResidingAirport(String airplaneCode, LocalDateTime departureDate) {
        try {
            AirplaneDTO airplaneDTO = getAirplaneByAirplaneCode(airplaneCode);
            Airplane airplane = toAirplane(airplaneDTO);
            Set<Flight> flights = airplane.getFlights();

            LocalDateTime rightDate = null;
            Airport currentResidingAirport = null;

            for (Flight flight : flights.stream().sorted(Comparator.comparing(Flight::getEtd)).toList()) {
                LocalDateTime date1 = flight.getEtd();
                LocalDateTime date2 = flight.getEta();

                if (departureDate.isAfter(date1) && departureDate.isBefore(date2)) {
                    return null;
                }

                if (date2.isBefore(departureDate)) {
                    rightDate = date2;
                    currentResidingAirport = flight.getArrivalAirport();
                }
            }

            return (rightDate != null) ? AirportService.fromAirport(currentResidingAirport) : AirportService.fromAirport(airplane.getResidingAirport());
        } catch (Exception e) {
            throw new RuntimeException("Failed to get current airport");
        }
    }

    public void saveAirplane(AirplaneDTO airplaneDTO) {
        try {
            if (airplaneDTO == null) {
                throw new NotFoundException("Airplane not found");
            }

            Airplane airplane = toAirplane(airplaneDTO);
            airplaneRepository.save(airplane);
        } catch (Exception e) {
            throw new RuntimeException("Failed to save airport");
        }
    }

    public List<Airplane> saveAirplanes(List<Airplane> airplanes) {
        return airplaneRepository.saveAll(airplanes);
    }

    public void deleteAirplane(String airplaneCode) {
        airplaneRepository.deleteById(airplaneCode);
    }

    public static AirplaneDTO fromAirplane(Airplane airplane) {
        var dto = new AirplaneDTO();

        dto.setAirplaneCode(airplane.getAirplaneCode());
        dto.setAirplaneType(airplane.getAirplaneType());
        dto.setAirplaneCapacity(airplane.getAirplaneCapacity());
        dto.setResidingAirport(airplane.getResidingAirport().getAirportName());
        dto.setFlights(airplane.getFlights());

        return dto;
    }

    public Airplane toAirplane(AirplaneDTO dto) {
        var airplane = new Airplane();

        airplane.setAirplaneCode(dto.getAirplaneCode());
        airplane.setAirplaneType(dto.getAirplaneType());
        airplane.setAirplaneCapacity(dto.getAirplaneCapacity());

        Airport airport = airportService.findByAirportName(dto.getResidingAirport());
        airplane.setResidingAirport(airport);

        return airplane;
    }

}
