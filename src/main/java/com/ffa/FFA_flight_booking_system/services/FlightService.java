package com.ffa.FFA_flight_booking_system.services;

import com.ffa.FFA_flight_booking_system.dto.FlightDTO;
import com.ffa.FFA_flight_booking_system.models.Airplane;
import com.ffa.FFA_flight_booking_system.models.Airport;
import com.ffa.FFA_flight_booking_system.models.Flight;
import com.ffa.FFA_flight_booking_system.repositories.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public Flight saveFlight(Flight flight) {
        return flightRepository.save(flight);
    }

    public List<Flight> saveFlights(List<Flight> flights) {
        return flightRepository.saveAll(flights);
    }

    public FlightDTO getFlightByFlightNumber(String flightNumber) {
        Flight flight = flightRepository.findByFlightNumber(flightNumber);
        return fromFlight(flight);
    }

    public static FlightDTO fromFlight(Flight flight) {
        var dto = new FlightDTO();

        dto.setFlightNumber(flight.getFlightNumber());
        dto.setEta(flight.getEta());
        dto.setEtd(flight.getEtd());
        dto.setAirplaneCode(flight.getAirplane().getAirplaneCode());
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

        Airport dep_airport = airportService.findByAirportName(dto.getDep_airport_name());
        Airport arr_airport = airportService.findByAirportName(dto.getArr_airport_name());
        flight.setDepartureAirport(dep_airport);
        flight.setArrivalAirport(arr_airport);

        return flight;
    }
}
