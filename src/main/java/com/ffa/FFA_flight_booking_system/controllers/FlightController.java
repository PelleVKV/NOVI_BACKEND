package com.ffa.FFA_flight_booking_system.controllers;

import com.ffa.FFA_flight_booking_system.dto.AirportDTO;
import com.ffa.FFA_flight_booking_system.dto.FlightDTO;
import com.ffa.FFA_flight_booking_system.exceptions.NotFoundException;
import com.ffa.FFA_flight_booking_system.models.Airplane;
import com.ffa.FFA_flight_booking_system.models.Flight;
import com.ffa.FFA_flight_booking_system.services.AirplaneService;
import com.ffa.FFA_flight_booking_system.services.FlightService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping
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

    @GetMapping("/flight/all")
    public ResponseEntity<ArrayList<FlightDTO>> getAllFlights() {
        return new ResponseEntity<>(new ArrayList<>(flightService.getAllFlights()), HttpStatus.OK);
    }

    @GetMapping("/flight/{flightNumber}")
    public ResponseEntity<FlightDTO> getFlightByFlightNumber(@PathVariable String flightNumber) throws NotFoundException {
        return ResponseEntity.ok().body(flightService.getFlightByFlightNumber(flightNumber));
    }

    // POST MAPPING, SENDING DATA

    @PostMapping(value = "/atc/flight", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createFlight(@RequestBody FlightDTO flightDTO) throws NotFoundException {
        try {
            Flight flight = flightService.toFlight(flightDTO);

            // Validation check
            if (flight == null) {
                return ResponseEntity.badRequest().body("Error: invalid flight data");
            }

            // If the selected airplane is not at the selected airport at the selected departure date, not available
            AirportDTO airportDTO = airplaneService.getResidingAirport(flightDTO.getAirplaneCode(), flightDTO.getEtd());
            if (!flightDTO.getDepAirportName().equals(airportDTO.getAirportName())) {
                return ResponseEntity.badRequest().body("Error: selected airplane is not at selected airport at selected time");
            }

            // Clear representation of action result
            flightService.saveFlight(flightDTO);
            return ResponseEntity.ok().body("Success: created flight.");
        } catch (Exception e) {
            logger.error("Error creating flight: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body("Error: error creating flight; " + e.getMessage());
        }
    }

    // PUT MAPPING, CHANGING DATA

    @PutMapping("/atc/flight/{flightNumber}")
    public ResponseEntity<String> updateFlight(@PathVariable String flightNumber, @RequestBody FlightDTO updatedFlightDTO) {
        try {
            flightService.updateFlight(flightNumber, updatedFlightDTO);
            return ResponseEntity.ok("Flight updated successfully");
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Flight not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating flight");
        }
    }

    // DELETE MAPPING, DELETING DATA

    @DeleteMapping("/atc/flight/{flightNumber}")
    public ResponseEntity<String> deleteFlight(@PathVariable String flightNumber) {
        try {
            FlightDTO flightDTO = flightService.getFlightByFlightNumber(flightNumber);

            // Validation check
            if (flightDTO == null) {
                return ResponseEntity.badRequest().body("Error: invalid flight data");
            }

            flightService.deleteFlight(flightDTO);
            return ResponseEntity.ok().body("Success: deleted flight (" + flightNumber + ")");
        } catch (Exception e) {
            logger.error("Error deleting flight: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body("Error: error deleting flight; " + e.getMessage());
        }
    }

    @DeleteMapping("/atc/flight/all")
    public ResponseEntity<String> deleteAllFlights() {
        try {
            flightService.deleteAllFlights();
            return ResponseEntity.ok().body("Success: deleted all flights");
        } catch (Exception e) {
            logger.error("Error deleting all flights: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body("Error: error deleting all flights; " + e.getMessage());
        }
    }

}