package com.ffa.FFA_flight_booking_system.services;

import com.ffa.FFA_flight_booking_system.dto.AirportDTO;
import com.ffa.FFA_flight_booking_system.dto.FlightDTO;
import com.ffa.FFA_flight_booking_system.exceptions.CapacityExceededException;
import com.ffa.FFA_flight_booking_system.exceptions.NotFoundException;
import com.ffa.FFA_flight_booking_system.models.Airplane;
import com.ffa.FFA_flight_booking_system.models.Airport;
import com.ffa.FFA_flight_booking_system.models.Flight;
import com.ffa.FFA_flight_booking_system.models.Reservation;
import com.ffa.FFA_flight_booking_system.repositories.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class FlightService {
    private final FlightRepository flightRepository;
    private final AirportService airportService;
    private final AirplaneService airplaneService;

    @Autowired
    public FlightService(FlightRepository flightRepository, AirportService airportService, AirplaneService airplaneService) {
        this.flightRepository = flightRepository;
        this.airportService = airportService;
        this.airplaneService = airplaneService;
    }

    public List<Flight> getAllFlights() {
        return flightRepository.findAll();
    }

    public FlightDTO saveFlight(FlightDTO flightDTO) {
        try {
            if (flightDTO == null) {
                throw new NotFoundException("Flight not found");
            }

            Flight flight = toFlight(flightDTO);
            if (flight == null) {
                throw new RuntimeException("Failed to create flight. Invalid FlightDTO provided.");
            }

            flightRepository.save(flight);
            return flightDTO;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create flight", e);
        }
    }

    public FlightDTO getFlightByFlightNumber(String flightNumber) {
        Flight flight = flightRepository.findByFlightNumber(flightNumber);
        return fromFlight(flight);
    }

    @Transactional
    public void incrementFilledSeats(String flightNumber, int seatsToAdd) throws CapacityExceededException, NotFoundException {
        Flight flight = flightRepository.findByFlightNumber(flightNumber);

        if (flight != null) {
            int currentFilledSeats = flight.getFilledSeats();
            int newFilledSeats = currentFilledSeats + seatsToAdd;

            int maxCapacity = flight.getAirplane().getAirplaneCapacity();
            if (newFilledSeats <= maxCapacity) {
                flight.setFilledSeats(newFilledSeats);
                flightRepository.save(flight);
            } else {
                throw new CapacityExceededException("Adding seats exceeds the airplane capacity.");
            }
        } else {
            // Handle case where flight is not found (optional)
            throw new NotFoundException("Flight with number " + flightNumber + " not found.");
        }
    }

    public static FlightDTO fromFlight(Flight flight) {
        var dto = new FlightDTO();

        dto.setFlightNumber(flight.getFlightNumber());
        dto.setEta(flight.getEta());
        dto.setEtd(flight.getEtd());
        dto.setAirplaneCode(flight.getAirplane().getAirplaneCode());
        dto.setFilledSeats(flight.getFilledSeats());
        dto.setDep_airport_name(flight.getDepartureAirport().getAirportName());
        dto.setArr_airport_name(flight.getArrivalAirport().getAirportName());

        return dto;
    }

    public Flight toFlight(FlightDTO dto) {
        var flight = new Flight();

        flight.setFlightNumber(dto.getFlightNumber());
        flight.setEta(dto.getEta());
        flight.setEtd(dto.getEtd());

        Airplane airplane = airplaneService.getAirplaneByAirplaneCode(dto.getAirplaneCode());
        flight.setAirplane(airplane);

        flight.setFilledSeats(dto.getFilledSeats());

        AirportDTO dep_airport = airportService.findByAirportName(dto.getDep_airport_name());
        AirportDTO arr_airport = airportService.findByAirportName(dto.getArr_airport_name());
        flight.setDepartureAirport(airportService.toAirport(dep_airport));
        flight.setArrivalAirport(airportService.toAirport(arr_airport));

        return flight;
    }
}
