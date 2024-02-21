package com.ffa.FFA_flight_booking_system.controllers;

import com.ffa.FFA_flight_booking_system.dto.AirportDTO;
import com.ffa.FFA_flight_booking_system.dto.FlightDTO;
import com.ffa.FFA_flight_booking_system.exceptions.CapacityExceededException;
import com.ffa.FFA_flight_booking_system.models.Airplane;
import com.ffa.FFA_flight_booking_system.models.Flight;
import com.ffa.FFA_flight_booking_system.services.AirplaneService;
import com.ffa.FFA_flight_booking_system.services.AirportService;
import com.ffa.FFA_flight_booking_system.services.FlightService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@RestController
@CrossOrigin("*")
@RequestMapping("/flights")
public class FlightController {
    private static final Logger logger = LoggerFactory.getLogger(ReservationController.class);
    private final FlightService flightService;
    private final AirplaneService airplaneService;

    @Autowired
    public FlightController(FlightService flightService, AirplaneService airplaneService) {
        this.flightService = flightService;
        this.airplaneService = airplaneService;
    }

    // GET MAPPING, RETRIEVING DATA

    @GetMapping
    public ResponseEntity<ArrayList<Flight>> getAllFlights() {
        return new ResponseEntity<>(new ArrayList<>(flightService.getAllFlights()), HttpStatus.OK);
    }

    @GetMapping("/{flightNumber}")
    public ResponseEntity<Object> getFlightByFlightNumber(@PathVariable String flightNumber) {
        return ResponseEntity.ok().body(flightService.getFlightByFlightNumber(flightNumber));
    }

    // POST MAPPING, SENDING DATA

    @PostMapping(value = "/create_flight", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FlightDTO> createFlight(@RequestBody FlightDTO flightDTO) {
        try {
            Flight flight = flightService.toFlight(flightDTO);

            // Validation check
            if (flight == null) {
                return ResponseEntity.notFound().build();
            }

            // If the selected airplane is not at the selected airport at the selected departure date, not available
            Airplane airplane = flight.getAirplane();
            AirportDTO airportDTO = airplaneService.getResidingAirport(airplane.getAirplaneCode(), flight.getEtd());
            if (!flight.getDepartureAirport().getAirportName().equals(airportDTO.getAirportName())) {
                System.out.println("airports dont match up");
                return ResponseEntity.badRequest().build();
            }

            return ResponseEntity.ok().body(flightService.saveFlight(flightDTO));
        } catch (Exception e) {
            logger.error("Error creating flight with number: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

}
