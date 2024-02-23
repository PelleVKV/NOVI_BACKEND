package com.ffa.FFA_flight_booking_system.services;

import com.ffa.FFA_flight_booking_system.controllers.ReservationController;
import com.ffa.FFA_flight_booking_system.dto.AirplaneDTO;
import com.ffa.FFA_flight_booking_system.dto.AirportDTO;
import com.ffa.FFA_flight_booking_system.dto.FlightDTO;
import com.ffa.FFA_flight_booking_system.dto.ReservationDTO;
import com.ffa.FFA_flight_booking_system.exceptions.CapacityExceededException;
import com.ffa.FFA_flight_booking_system.exceptions.NotFoundException;
import com.ffa.FFA_flight_booking_system.models.Airplane;
import com.ffa.FFA_flight_booking_system.models.Airport;
import com.ffa.FFA_flight_booking_system.models.Flight;
import com.ffa.FFA_flight_booking_system.models.Reservation;
import com.ffa.FFA_flight_booking_system.repositories.FlightRepository;
import com.ffa.FFA_flight_booking_system.repositories.ReservationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class FlightService {
    private static final Logger logger = LoggerFactory.getLogger(ReservationController.class);
    private final FlightRepository flightRepository;
    private final AirportService airportService;
    private final AirplaneService airplaneService;
    private final ReservationRepository reservationRepository;

    @Autowired
    public FlightService(FlightRepository flightRepository, AirportService airportService, AirplaneService airplaneService, ReservationRepository reservationRepository) {
        this.flightRepository = flightRepository;
        this.airportService = airportService;
        this.airplaneService = airplaneService;
        this.reservationRepository = reservationRepository;
    }

    public List<FlightDTO> getAllFlights() {
        List<Flight> flights = flightRepository.findAll();
        List<FlightDTO> dtos = new ArrayList<>();
        for (Flight flight : flights) {
            dtos.add(fromFlight(flight));
        }
        return dtos;
    }

    public void saveFlight(FlightDTO flightDTO) {
        try {
            if (flightDTO == null) {
                throw new NotFoundException("Flight not found");
            }

            Flight flight = toFlight(flightDTO);
            if (flight == null) {
                throw new RuntimeException("Failed to create flight. Invalid FlightDTO provided.");
            }

            flightRepository.save(flight);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create flight", e);
        }
    }

    @Transactional
    public void deleteFlight(FlightDTO flightDTO) throws NotFoundException {
        Flight flight = toFlight(flightDTO);
        if (flightRepository.existsById(flight.getFlightNumber())) {
            reservationRepository.deleteReservationsByFlight(flight);
            flightRepository.deleteWithQuery(flight.getFlightNumber());
        } else {
            throw new NotFoundException("Flight not found");
        }
    }

    public void deleteAllFlights() {
        try {
            flightRepository.deleteAll();
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete all flights", e);
        }
    }

    public FlightDTO getFlightByFlightNumber(String flightNumber) {
        Flight flight = flightRepository.findByFlightNumber(flightNumber);
        return fromFlight(flight);
    }

    @Transactional
    public void updateFlight(String flightNumber, FlightDTO updatedFlightDTO) throws NotFoundException {
        Flight existingFlight = flightRepository.findByFlightNumber(flightNumber);

        if (existingFlight != null) {
            existingFlight.setFlightNumber(updatedFlightDTO.getFlightNumber());
            existingFlight.setFilledSeats(updatedFlightDTO.getFilledSeats());
            existingFlight.setEtd(updatedFlightDTO.getEtd());
            existingFlight.setEta(updatedFlightDTO.getEta());

            flightRepository.save(existingFlight);
        } else {
            throw new NotFoundException("Flight not found");
        }
    }

    @Transactional
    public void updateFilledSeats(String flightNumber, int seatsToUpdate) throws CapacityExceededException, NotFoundException {
        Flight flight = flightRepository.findByFlightNumber(flightNumber);

        if (flight != null) {
            int currentFilledSeats = flight.getFilledSeats();
            int newFilledSeats = currentFilledSeats + seatsToUpdate;

            int maxCapacity = flight.getAirplane().getAirplaneCapacity();
            if (newFilledSeats >= 0 && newFilledSeats <= maxCapacity) {
                flight.setFilledSeats(newFilledSeats);
                flightRepository.save(flight);
            } else {
                throw new CapacityExceededException("Updating seats exceeds the airplane capacity.");
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
        dto.setDepAirportName(flight.getDepartureAirport().getAirportName());
        dto.setArrAirportName(flight.getArrivalAirport().getAirportName());
        dto.setReservations(flight.getReservations());

        return dto;
    }

    public Flight toFlight(FlightDTO dto) throws NotFoundException {
        var flight = new Flight();

        flight.setFlightNumber(dto.getFlightNumber());
        flight.setEta(dto.getEta());
        flight.setEtd(dto.getEtd());
        flight.setFilledSeats(dto.getFilledSeats());

        AirplaneDTO airplaneDTO = airplaneService.getAirplaneByAirplaneCode(dto.getAirplaneCode());
        Airplane airplane = airplaneService.toAirplane(airplaneDTO);
        flight.setAirplane(airplane);

        Airport dep_airport = airportService.findByAirportName(dto.getDepAirportName());
        flight.setDepartureAirport(dep_airport);
        Airport arr_airport = airportService.findByAirportName(dto.getArrAirportName());
        flight.setArrivalAirport(arr_airport);

        flight.setReservations(dto.getReservations());

        return flight;
    }
}
